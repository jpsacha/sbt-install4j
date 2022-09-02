/*
 * Copyright 2014-2022 Jarek Sacha (jpsacha -at- gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ij_plugins.sf.sbt.install4j

import sbt.*
import sbt.Keys.*
import sbt.complete.DefaultParsers.*

import java.io.{File, IOException}
import scala.collection.mutable
import scala.sys.process.Process

/** SBT plugin for building installers with Install4J. */
object SBTInstall4J extends sbt.AutoPlugin {

  object autoImport {
    lazy val install4j: InputKey[Unit] = InputKey[Unit](
      "install4j",
      "Builds Install4J project. " +
        "It can take optional argument that are passed by to the Install4J compiler. " +
        "Refer to `install4jc` documentation in Install4J Help for list of supported command line options."
    )

    lazy val install4jCopyDependedJars: TaskKey[File] = TaskKey[File](
      "install4jCopyDependedJars",
      "Copies project dependencies to directory `install4jDependedJarsDir`."
    )

    lazy val install4jCopyDependedJarsExclusions: SettingKey[Seq[String]] = SettingKey[Seq[String]](
      "install4jCopyDependedJarsExclusions",
      "List of regex expressions that match files that will be excluded from copying."
    )

    lazy val install4jCopyDependedJarsEnabled: SettingKey[Boolean] = SettingKey[Boolean](
      "install4jCopyDependedJarsEnabled",
      "if `true` dependent jars will be copies, if `false` they will be not."
    )

    lazy val install4jHomeDir: SettingKey[File] = SettingKey[File](
      "install4jHomeDir",
      "Deprecated. Install4J installation directory. " +
        "It assumes that Install4J compiler is in subdirectory `bin`. " +
        s"Default can be set with environment variable ${Defaults.INSTALL4J_HOME_ENV}. " +
        s"This option is deprecated, use environment variable ${Defaults.INSTALL4JC_FILE_ENV} " +
        "or setting `install4jcFile` instead."
    )

    lazy val install4jcFile: SettingKey[File] = SettingKey[File](
      "install4jcFile",
      "Location of the install4j's command line compiler `install4jc[.exe]`. " +
        "It can be found in the `bin` directory of the install4j installation. " +
        s"Default can be set with environment variable ${Defaults.INSTALL4JC_FILE_ENV}." +
        ""
    )

    lazy val install4jProjectFile: SettingKey[String] = SettingKey[String](
      "install4jProjectFile",
      "Relative path to the install4j project file that should be build."
    )

    lazy val install4jDependedJarsDir: SettingKey[String] = SettingKey[String](
      "install4jDependedJarsDir",
      "Location where dependent jars will be copied."
    )

    lazy val install4jExtraOptions: SettingKey[Seq[String]] = SettingKey[Seq[String]](
      "install4jExtraOptions",
      "Additional command line options passed to the compiler."
    )

    lazy val install4jVerbose: SettingKey[Boolean] = SettingKey[Boolean](
      "install4jVerbose",
      "Enables verbose mode."
    )

    lazy val install4jRelease: SettingKey[String] = SettingKey[String](
      "install4jRelease",
      "Override the application version. " +
        "Version number components can be alphanumeric and should be separated by dots, dashes or underscores. "
    )

    lazy val install4jCompilerVariables: SettingKey[Map[String, String]] = SettingKey[Map[String, String]](
      "install4jCompilerVariables",
      "Override a compiler variable with a different value. " +
        "In the map, the `key` is variable's name, the `value` is variable's value."
      )
  }

  import net.ij_plugins.sf.sbt.install4j.SBTInstall4J.autoImport.*

  override def projectSettings: Seq[Def.Setting[?]] = Seq(
    install4j := {
      // get the result of parsing
      val extraArgs: Seq[String] = spaceDelimited("<arg>").parsed
      streams.value.log.debug("extraArgs: " + extraArgs.mkString(", "))

      // Run dependent tasks first
      val _v1 = (Compile / packageBin).value
      assert(_v1 != null)
      val _v2 = install4jCopyDependedJars.value
      assert(_v2 != null)

      val install4jCompiler = install4jcFile.value.getCanonicalFile
      val install4jProject  = new File(baseDirectory.value, install4jProjectFile.value).getCanonicalFile
      runInstall4J(
        install4jCompiler,
        install4jProject,
        verbose = install4jVerbose.value,
        release = install4jRelease.value,
        compilerVariables = install4jCompilerVariables.value,
        extraOptions = install4jExtraOptions.value,
        extraArgs = extraArgs,
        streams.value
        )
    },
    install4jCopyDependedJars := copyDependedJars(
      (Runtime / dependencyClasspath).value,
      crossTarget.value,
      install4jCopyDependedJarsExclusions.value,
      streams.value
      ),
    install4jCopyDependedJarsExclusions := Seq(
      // Source archives
      """\S*-src\.\S*""",
      """\S*-sources\.\S*""",
      """\S*_src_\S*""",
      // Javadoc
      """\S*-javadoc\.\S*""",
      """\S*_javadoc_\S*""",
      // Scaladoc
      """\S*-scaladoc\.\S*""",
      """\S*_scaladoc_\S*"""
      ),
    install4jCopyDependedJarsEnabled := true,
    install4jExtraOptions := Seq.empty[String],
    install4jHomeDir := file(Defaults.install4jHomeDir().getCanonicalPath),
    install4jcFile := file(Defaults.install4jCompilerFile(install4jHomeDir.value).getCanonicalPath),
    install4jProjectFile := "installer/installer.install4j",
    install4jVerbose := false,
    install4jRelease := "",
    install4jCompilerVariables := Map.empty
    )

  private def prefix = "[sbt-install4j] "

  private def copyDependedJars(
    libs: Seq[Attributed[File]],
    crossTargetDir: File,
    exclusions: Seq[String],
    taskStreams: TaskStreams
  ): File = {
    val logger = taskStreams.log
    val libDir = crossTargetDir / "lib"
    libs.foreach { lib =>
      val file = lib.data
      if (file.exists) {
        if (file.isDirectory) {
          logger.warn(prefix + "Dependent directories not supported. Consider using `exportJars := true`")
        } else if (exclusions.forall(!file.name.matches(_))) {
          if (file.name.toLowerCase.endsWith(".jar")) {
            IO.copyFile(file, libDir / file.name)
          } else {
            logger.warn(prefix + "Skipping non *.jar: " + file.absolutePath)
          }
        }
      }
    }

    libDir
  }

  private def runInstall4J(
    compiler: File,
    project: File,
    verbose: Boolean,
    release: String,
    compilerVariables: Map[String, String],
    extraOptions: Seq[String],
    extraArgs: Seq[String],
    taskStreams: TaskStreams
  ): Unit = {
    val logger = taskStreams.log

    logger.debug(prefix + "compiler          : " + compiler.getAbsolutePath)
    if (!compiler.exists) throw new IOException(
      "Install4J Compiler not found at: " + compiler.getAbsolutePath
    )

    logger.debug(prefix + "project           : " + project.getAbsolutePath)
    if (!project.exists) {
      throw new IOException("install4j project file not found: " +
        "" + project.getAbsolutePath)
    }

    logger.debug(prefix + "release          : " + release)
    logger.debug(prefix + "compilerVariables: " + compilerVariables.size)
    compilerVariables.foreach { case (k, v) =>
      logger.debug(prefix + s"  compilerVariable $k: $v")
    }
    logger.debug(prefix + "extraOptions     : " + extraOptions.size)
    extraOptions.foreach { v => logger.debug(prefix + s"  extraOption: $v") }
    logger.debug(prefix + "extraArgs        : " + extraArgs.size)
    extraArgs.foreach { v => logger.debug(prefix + s"  extraArgs: $v") }

    val commandLine = mutable.ListBuffer.empty[String]

    commandLine += compiler.getCanonicalPath

    // Verbose
    if (verbose) commandLine += "--verbose"

    // Release
    if (release.trim.nonEmpty) commandLine += "--release=" + release.trim

    // Compiler variables
    if (compilerVariables.nonEmpty) {
      commandLine += "-D"
      commandLine += "\"" +
        compilerVariables.map {
          case (k, v) => k.trim + "=" + v.trim
        }.mkString(",") +
        "\""
    }

    commandLine += project.getPath

    commandLine ++= extraOptions

    commandLine ++= extraArgs

    logger.debug(prefix + "executing command: " + commandLine.mkString(" "))
    val output = Process(commandLine).lineStream
    output.foreach(println)
  }
}
