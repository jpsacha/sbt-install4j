// Plugin settings must be usable from a `.sbt` file, this fails to load if `autoImport` is not accessible.
// Regression test for `private object autoImport` (commit d93a3f6).

name         := "simple"
version      := "0.1"
scalaVersion := "2.12.17"
exportJars   := true

enablePlugins(SBTInstall4J)
install4jProjectFile       := "installer/simple.install4j"
install4jVerbose           := true
install4jRelease           := version.value
install4jCompilerVariables := Map("foo" -> "bar")
install4jExtraOptions      := Seq("--test")

lazy val checkSettings = taskKey[Unit]("Check that plugin settings have the expected values")
checkSettings := {
  assert(install4jProjectFile.value == "installer/simple.install4j", install4jProjectFile.value)
  assert(install4jVerbose.value)
  assert(install4jRelease.value == "0.1", install4jRelease.value)
  assert(install4jCompilerVariables.value == Map("foo" -> "bar"), install4jCompilerVariables.value)
  assert(install4jExtraOptions.value == Seq("--test"), install4jExtraOptions.value)
  val compiler = install4jcFile.value.getName
  assert(compiler == "install4jc" || compiler == "install4jc.exe", compiler)
}
