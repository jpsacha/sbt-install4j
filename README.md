sbt-install4j
=============

SBT plugin for building installers with Install4J


Sample use, add following to your `build.sbt`:

```scala
import Install4JKeys._

install4jSettings

install4jProjectFile := "installer/example.install4j"

install4jVerbose := true

install4jRelease := version.value
```

SBT tasks provided by  `sbt-install4j` plugin:

* `install4j` : Task - 
  Builds Install4J project

* `install4jCopyDependedJars` : Task - 
  Copies project dependencies to directory `install4jDependedJarsDir`
  

SBT settings provided by  `sbt-install4j` plugin:

* `install4jCopyDependedJarsExclusions` : Seq[String] - 
  List of regex expressions that match files that will be excluded from copying.

* `install4jCopyDependedJarsEnabled` : Boolean -
   if `true` dependent jars will be copies, if `false` they will be not.

* `install4jHomeDir` : File -
  Install4J installation directory. It assumes that Install4J compiler is in subdirectory `bin/install4jc.exe`

* `install4jProjectFile` : String - The install4j project file that should be build.

* `install4jDependedJarsDir` : String -
  Location where dependent jars will be copied.

* `install4jVerbose` : Boolean -
  Enables verbose mode.

* `install4jRelease` : String -
  Override the application version. 
  Version number components can be alphanumeric and should be separated by dots, dashes or underscores.

* `install4jCompilerVariables` : Map[String, String] -
  Override a compiler variable with a different value.
  In the map, the `key` is variable's name, the `value` is variable's value.


