sbt-install4j
=============

[![Build Status](https://travis-ci.org/jpsacha/sbt-install4j.svg?branch=master)](https://travis-ci.org/jpsacha/sbt-install4j)   [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jpsacha/sbt-install4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.jpsacha/sbt-install4j)


[SBT] plugin for building installers with [Install4J].


Usage
-----

`sbt-install4j` is available for sbt-1.x.

### project/plugins.sbt

Import `sbt-install4j` plugin to use `install4j` command.

```scala
addSbtPlugin("com.github.jpsacha" % "sbt-install4j" % "1.2.0")
```

### build.sbt
Sample use, add following to your `build.sbt`:

```scala
enablePlugins(SBTInstall4J)
install4jProjectFile := "installer/example.install4j"
```

## Available Tasks and Settings

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

* `install4jcFile` : File -  Location of the install4j's command line compiler `install4jc[.exe]`. It can be found in the `bin` directory of the install4j installation. Default can be set with environment variable `INSTALL4JC_FILE`.

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
  
* `install4jHomeDir` : File - __Deprecated__. Install4J installation directory. It assumes that Install4J compiler is in subdirectory `bin`. Default can be set with environment variable `INSTALL4J_HOME`. This option is deprecated, use environment variable `INSTALL4JC_FILE` or setting `install4jcFile` instead.
 
## Determining location of Install4J libraries and compiler

The `sbt-install4j` executes Install4J compiler. It needs to know its location. It will attempts to determine location base on the OS used. On Windows it is assumed to be:
```
C:\Program Files\Install4J7\bin\intall4jc.exe

```

On Mac OS X:
```
/Applications/install4j.app/Contents/Resources/app/bin/install4jc
```

On Linux:
```
/opt/install4j/bin/install4jc
```

If the Install4J is installed in a different location you can specify location of the compiler using the environment variable `INSTALL4JC_FILE` or setting `install4jcFile`. For multi-platform builds it is preferred to use the environment variable `INSTALL4JC_FILE`.





[Install4J]: https://www.ej-technologies.com/products/install4j/overview.html
[SBT]: http://www.scala-sbt.org/