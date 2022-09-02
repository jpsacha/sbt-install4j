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

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

import java.io.File

class DefaultsTest extends AnyFlatSpec {

  it should "determine install4jHomeDir from environment variable" in {
    val f         = File.createTempFile("_env_", "_env_")
    val path_name = f.getCanonicalPath
    System.setProperty(Defaults.INSTALL4J_HOME_ENV, path_name)

    val install4jHomeDir = Defaults.install4jHomeDir()

    install4jHomeDir should be(f)
  }

  it should "determine install4jCompilerFile from environment variable" in {
    val f         = File.createTempFile("_env_", "_env_")
    val path_name = f.getCanonicalPath
    System.setProperty(Defaults.INSTALL4JC_FILE_ENV, path_name)

    val install4jCompilerFile = Defaults.install4jCompilerFile(Defaults.install4jHomeDir()).getCanonicalPath

    install4jCompilerFile should be(path_name)
  }

}
