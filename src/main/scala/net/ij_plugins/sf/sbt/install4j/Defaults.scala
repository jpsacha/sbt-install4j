package net.ij_plugins.sf.sbt.install4j

import java.io.File

object Defaults {

  val INSTALL4J_HOME_ENV = "INSTALL4J_HOME"
  val INSTALL4JC_FILE_ENV = "INSTALL4JC_FILE"

  def install4jHomeDir(): String = {
    // First check for INSTALL4J_HOME, and if available use that
    Option(System.getProperty(INSTALL4J_HOME_ENV, null)) match {
      case Some(s) => s
      case _ =>
        val osName = System.getProperty("os.name")

        if (osName.startsWith("Windows"))
          "C:/Program Files/install4j7"
        else if (osName.equals("Linux"))
          "/opt/install4j7"
        else if (osName.equals("Mac OS X"))
          "/Applications/install4j.app/Contents/Resources/app"
        else
          throw new UnsupportedOperationException(
            "Cannot determine default 'Install4jHomeDir'. Unsupported OS: " + osName)
    }
  }

  def install4jCompilerFile(install4jHomeDir: String = Defaults.install4jHomeDir()): File = {
    // First check for INSTALL4JC_PATH, and if available use that
    Option(System.getProperty(INSTALL4JC_FILE_ENV, null)) match {
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
