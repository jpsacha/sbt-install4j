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


import java.io.File

object Defaults {

  val INSTALL4J_HOME_ENV  = "INSTALL4J_HOME"
  val INSTALL4JC_FILE_ENV = "INSTALL4JC_FILE"

  private val debugMode = false

  private def debug(msg: String): Unit =
    if (debugMode) println(s"[install4j] $msg")

  private def getPropOrEnv(name: String): Option[String] = {
    val prop = System.getProperty(name, null)
    debug(s"system property $name: $prop")
    val r = {
      if (prop == null) {
        val env = System.getenv(name)
        debug(s"environmental variable $name: $env")
        env
      } else {
        prop
      }
    }
    Option(r)
  }

  def install4jHomeDir(): File = {

    // First check for INSTALL4J_HOME, and if available use that
    val install4JHomeEnv = getPropOrEnv(INSTALL4J_HOME_ENV)

    debug(s"$INSTALL4J_HOME_ENV: $install4JHomeEnv")

    val r = install4JHomeEnv match {
      case Some(s) => s
      case _ =>
        val osName = System.getProperty("os.name")

        if (osName.startsWith("Windows"))
          "C:/Program Files/install4j9"
        else if (osName.equals("Linux"))
          "/opt/install4j9"
        else if (osName.equals("Mac OS X"))
          "/Applications/install4j.app/Contents/Resources/app"
        else
          throw new UnsupportedOperationException(
            "Cannot determine default 'Install4jHomeDir'. Unsupported OS: " + osName
            )
    }

    debug(s"install4jHomeDir: $r")
    new File(r)
  }

  def install4jCompilerFile(install4jHomeDir: File): File = {

    val installJCFileEnv = getPropOrEnv(INSTALL4JC_FILE_ENV)
    debug(s"$INSTALL4JC_FILE_ENV: $installJCFileEnv")

    // First check for INSTALL4JC_PATH, and if available use that
    val r = installJCFileEnv match {
      case Some(s) => new File(s)
      case _ => new File(install4jHomeDir, "bin/" + compilerName())
    }
    debug(s"install4jCompilerFile: $r")
    r
  }

  def compilerName(): String = {
    if (System.getProperty("os.name").startsWith("Windows"))
      "install4jc.exe"
    else
      "install4jc"
  }
}
