/*
 * Copyright (C) 2014 Jarek Sacha
 * email: jpsacha at gmail dot com
 *
 * This file is part of sbt-install4j.
 */

package net.ij_plugins.sf.sbt.install4j

import sbt._
import sbt.Keys._
import java.io.{IOException, File}

/** SBT plugin that helps create runtime directory structure for ImageJ plugin development. */
object Plugin extends sbt.Plugin {

  import Install4JKeys._

  object Install4JKeys {
    lazy val install4j = TaskKey[Unit]("install4j", "Builds Install4J project.")

    lazy val install4jHomeDir = SettingKey[File]("install4jHomeDir",
      "Install4J installation directory. It assumes that Install4J compiler is in subdirectory `bin/install4jc.exe`")

    lazy val install4jProjectFile = SettingKey[String]("install4jProjectFile",
      "The install4j project file that should be build.")

    lazy val install4jVerbose = SettingKey[Boolean]("install4jVerbose","Enables verbose mode.")
  }

  lazy val install4jSettings: Seq[Def.Setting[_]] = Seq(

    install4j := {

      // Depends on `packageBin`
      val p = (packageBin in Compile).value

      val install4jCompiler = new File(install4jHomeDir.value, "bin/install4jc.exe").getCanonicalFile
      val install4jProject = new File(baseDirectory.value, install4jProjectFile.value).getCanonicalFile

      runInstall4J(
        install4jCompiler,
        install4jProject,
        verbose = install4jVerbose.value,
        streams.value)
    },

    install4jHomeDir := file("C:/Program Files/install4j5"),

    install4jProjectFile := "installer/installer.install4j",

    install4jVerbose := false
  )


  private def runInstall4J(compiler: File,
                           project: File,
                           verbose: Boolean,
                           taskStreams: TaskStreams) {
    val logger = taskStreams.log

    logger.debug("[install4j] compiler: " + compiler.getAbsolutePath)
    if (!compiler.exists) throw new IOException("Install4J Compiler not found: " + compiler.getAbsolutePath)

    logger.debug("[install4j] project: " + project.getAbsolutePath)
    if (!project.exists) {
      throw new IOException("install4j project file not found: " + project.getAbsolutePath)
    }

    var commandLine = "\"" + compiler.getPath + "\""
    if (verbose) commandLine += " --verbose "
    commandLine += "\"" + project.getPath + "\""

    logger.debug("[install4j] executing command: " + commandLine)
    val output = Process(commandLine).lines
    output.foreach(println)
  }
}
