/*
 * Copyright 2014-2026 Jarek Sacha (jpsacha -at- gmail.com)
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

  /** Value of system property `name`, if not set, value of environment variable `name`. */
  private def getPropOrEnv(name: String): Option[String] =
    Option(System.getProperty(name)).orElse(Option(System.getenv(name)))

  def install4jHomeDir(): File = {

    // First check for INSTALL4J_HOME, and if available use that
    val r = getPropOrEnv(INSTALL4J_HOME_ENV) match {
      case Some(s) => s
      case _ =>
        val osName = System.getProperty("os.name")

        // Do not throw for unrecognized OS, this is evaluated when the build loads.
        // A missing compiler is reported when `install4j` task runs.
        if (osName.startsWith("Windows"))
          "C:/Program Files/install4j13"
        else if (osName.equals("Mac OS X"))
          "/Applications/install4j.app/Contents/Resources/app"
        else
          // Linux and other Unix-like systems
          "/opt/install4j13"
    }

    new File(r)
  }

  def install4jCompilerFile(install4jHomeDir: File): File = {

    // First check for INSTALL4JC_FILE, and if available use that
    getPropOrEnv(INSTALL4JC_FILE_ENV) match {
      case Some(s) => new File(s)
      case _ => new File(install4jHomeDir, "bin/" + compilerName())
    }
  }

  def compilerName(): String = {
    if (System.getProperty("os.name").startsWith("Windows"))
      "install4jc.exe"
    else
      "install4jc"
  }
}
