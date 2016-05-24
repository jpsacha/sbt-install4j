//
// sbt-install4j plugin settings
//

enablePlugins(SBTInstall4J)
install4jProjectFile := "installer/example.install4j"
install4jVerbose := true
install4jRelease := version.value
