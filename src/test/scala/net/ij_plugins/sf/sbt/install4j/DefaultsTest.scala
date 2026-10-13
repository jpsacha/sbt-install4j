/*
 * Copyright 2014-2023 Jarek Sacha (jpsacha -at- gmail.com)
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

import java.nio.file.Files

class DefaultsTest extends AnyFlatSpec {

  /** Set system property `key` while executing `body`, then restore its previous state. */
  private def withSysProp[T](key: String, value: String)(body: => T): T = {
    val old = Option(System.getProperty(key))
    System.setProperty(key, value)
    try
      body
    finally
      old.fold(System.clearProperty(key))(System.setProperty(key, _))
  }

  it should "determine install4jHomeDir from environment variable" in {
    val f         = Files.createTempFile("_env_", "_env_").toFile
    val path_name = f.getCanonicalPath

    val install4jHomeDir = withSysProp(Defaults.INSTALL4J_HOME_ENV, path_name) {
      Defaults.install4jHomeDir()
    }

    install4jHomeDir.getCanonicalPath should be(path_name)
  }

  it should "determine install4jCompilerFile from environment variable" in {
    val f         = Files.createTempFile("_env_", "_env_").toFile
    val path_name = f.getCanonicalPath

    val install4jCompilerFile = withSysProp(Defaults.INSTALL4JC_FILE_ENV, path_name) {
      Defaults.install4jCompilerFile(Defaults.install4jHomeDir())
    }

    install4jCompilerFile.getCanonicalPath should be(path_name)
  }

}
