//
// sbt-install4j plugin settings
//
import Install4JKeys._

install4jSettings

install4jProjectFile := "installer/example.install4j"

install4jVerbose := true

install4jRelease := version.value
