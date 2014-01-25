/*
 * Copyright 2014 Jarek Sacha (jpsacha -at- gmail.com)
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

import sbt._
import sbt.Keys._
import java.io.{IOException, File}

/** SBT plugin for building installers with Install4J. */
object Plugin extends sbt.Plugin {

  import Install4JKeys._

  object Install4JKeys {
    lazy val install4j = TaskKey[Unit]("install4j",
      "Builds Install4J project.")

    lazy val install4jCopyDependedJars = TaskKey[File]("install4jCopyDependedJars",
      "Copies project dependencies to directory `install4jDependedJarsDir`")

    lazy val install4jCopyDependedJarsExclusions = SettingKey[Seq[String]]("install4jCopyDependedJarsExclusions",
      "List of regex expressions that match files that will be excluded from copying.")

    lazy val install4jCopyDependedJarsEnabled = SettingKey[Boolean]("install4jCopyDependedJarsEnabled",
      "if `true` dependent jars will be copies, if `false` they will be not.")

    lazy val install4jHomeDir = SettingKey[File]("install4jHomeDir",
      "Install4J installation directory. It assumes that Install4J compiler is in subdirectory `bin/install4jc.exe`")

    lazy val install4jProjectFile = SettingKey[String]("install4jProjectFile",
      "The install4j project file that should be build.")

    lazy val install4jDependedJarsDir = SettingKey[String]("install4jDependedJarsDir",
      "Location where dependent jars will be copied.")

    lazy val install4jVerbose = SettingKey[Boolean]("install4jVerbose",
      "Enables verbose mode.")

    lazy val install4jCompilerVariables = SettingKey[Map[String, String]]("install4jCompilerVariables",
      "Override a compiler variable with a different value. " +
        "In the map, the `key` is variable's name, the `value` is variable's value.")
  }

  lazy val install4jSettings: Seq[Def.Setting[_]] = Seq(
    install4j := {
      // Run dependent tasks first
      val _v1 = (packageBin in Compile).value
      assert(_v1 != null)
      val _v2 = install4jCopyDependedJars.value
      assert(_v2 != null)

      val install4jCompiler = new File(install4jHomeDir.value, "bin/install4jc.exe").getCanonicalFile
      val install4jProject = new File(baseDirectory.value, install4jProjectFile.value).getCanonicalFile
      runInstall4J(
        install4jCompiler,
        install4jProject,
        verbose = install4jVerbose.value,
        compilerVariables = install4jCompilerVariables.value,
        streams.value)
    },

    install4jCopyDependedJars := copyDependedJars(
      (dependencyClasspath in Runtime).value,
      crossTarget.value,
      install4jCopyDependedJarsExclusions.value,
      streams.value
    ),

    install4jCopyDependedJarsExclusions := Seq(
      // Source archives
      """\S*-src\.\S*""", """\S*-sources\.\S*""", """\S*_src_\S*""",
      // Javadoc
      """\S*-javadoc\.\S*""", """\S*_javadoc_\S*""",
      // Scaladoc
      """\S*-scaladoc\.\S*""", """\S*_scaladoc_\S*"""
    ),

    install4jCopyDependedJarsEnabled := true,

    install4jHomeDir := file("C:/Program Files/install4j5"),

    install4jProjectFile := "installer/installer.install4j",

    install4jVerbose := false,

    install4jCompilerVariables := Map.empty
  )

  private def prefix = "[sbt-install4j] "

  private def copyDependedJars(libs: Seq[Attributed[File]],
                               crossTargetDir: File,
                               exclusions: Seq[String],
                               taskStreams: TaskStreams): File = {
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

  private def runInstall4J(compiler: File,
                           project: File,
                           verbose: Boolean,
                           compilerVariables: Map[String, String],
                           taskStreams: TaskStreams) {
    val logger = taskStreams.log

    logger.debug(prefix + "compiler: " + compiler.getAbsolutePath)
    if (!compiler.exists) throw new IOException("Install4J Compiler not found: " + compiler.getAbsolutePath)

    logger.debug(prefix + "project: " + project.getAbsolutePath)
    if (!project.exists) {
      throw new IOException("install4j project file not found: " + project.getAbsolutePath)
    }

    var commandLine = "\"" + compiler.getPath + "\""
    if (verbose) commandLine += " --verbose "

    if (!compilerVariables.isEmpty) {
      commandLine += " -D \"" +
        compilerVariables.map {
          case (k, v) => k.trim + "=" + v.trim
        }.mkString(",") +
        "\""
    }

    commandLine += "\"" + project.getPath + "\""

    logger.debug(prefix + "executing command: " + commandLine)
    val output = Process(commandLine).lines
    output.foreach(println)
  }
}
