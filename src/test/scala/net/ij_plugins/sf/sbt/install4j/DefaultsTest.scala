package net.ij_plugins.sf.sbt.install4j

import java.io.File

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class DefaultsTest extends FlatSpec {

  it should "determine install4jHomeDir from environment variable" in {
    val f = File.createTempFile("_env_", "_env_")
    val path_name = f.getCanonicalPath
    System.setProperty(Defaults.INSTALL4J_HOME_ENV, path_name)

    val install4jHomeDir = Defaults.install4jHomeDir()

    install4jHomeDir should be(path_name)
  }

  it should "determine install4jCompilerFile from environment variable" in {
    val f = File.createTempFile("_env_", "_env_")
    val path_name = f.getCanonicalPath
    System.setProperty(Defaults.INSTALL4JC_FILE_ENV, path_name)

    val install4jCompilerFile = Defaults.install4jCompilerFile().getCanonicalPath

    install4jCompilerFile should be(path_name)
  }

}
