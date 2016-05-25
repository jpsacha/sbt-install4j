sbt-install4j
=============

[SBT] plugin for building installers with [Install4J].

Usage
-----

`sbt-install4j` is available for sbt-0.13.x.

### project/plugins.sbt

Import `sbt-install4j` plugin to use `install4j` command.

```scala
addSbtPlugin("com.github.jpsacha" % "sbt-install4j" % "1.1.0")
```

### build.sbt
Sample use, add following to your `build.sbt`:

```scala
enablePlugins(SBTInstall4J)
install4jProjectFile := "installer/example.install4j"
```

### Available Tasks and Settings

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


[Install4J]: https://www.ej-technologies.com/products/install4j/overview.html
[SBT]: http://www.scala-sbt.org/