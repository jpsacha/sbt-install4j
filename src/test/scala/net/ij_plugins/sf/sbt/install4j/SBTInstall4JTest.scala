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

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

import java.io.File

class SBTInstall4JTest extends AnyFlatSpec {

  private val compiler = new File("install4jc")
  private val project  = new File("installer/example.install4j")

  private def commandLine(compilerVariables: Map[String, String]): Seq[String] =
    SBTInstall4J.buildCommandLine(
      compiler,
      project,
      verbose = false,
      release = "",
      compilerVariables = compilerVariables,
      extraOptions = Seq.empty,
      extraArgs = Seq.empty
    )

  it should "pass compiler variables to -D without surrounding quotes" in {
    val cl = commandLine(Map("sys.version" -> "1.2.3", "foo" -> "bar"))

    val i = cl.indexOf("-D")
    i should be >= 0
    cl(i + 1) should be("sys.version=1.2.3,foo=bar")
  }

  it should "keep spaces in compiler variable values as a single argument" in {
    val cl = commandLine(Map("name" -> "My App"))

    cl(cl.indexOf("-D") + 1) should be("name=My App")
  }

  it should "omit -D when there are no compiler variables" in {
    commandLine(Map.empty) should not contain "-D"
  }

  it should "place the project file after the options" in {
    val cl = commandLine(Map("a" -> "b"))

    cl.head should be(compiler.getCanonicalPath)
    cl.last should be(project.getPath)
  }
}
