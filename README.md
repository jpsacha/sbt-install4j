sbt-install4j
=============

[![Build Status](https://travis-ci.org/jpsacha/sbt-install4j.svg?branch=master)](https://travis-ci.org/jpsacha/sbt-install4j)   [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jpsacha/sbt-install4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.jpsacha/sbt-install4j)


[SBT] plugin for building installers with [Install4J]   ![Install4J](https://www.ej-technologies.com/images/product_banners/install4j_medium.png)


Usage
-----

`sbt-install4j` is available for sbt-1.x.

### project/plugins.sbt

Import `sbt-install4j` plugin to use `install4j` command.

```scala
addSbtPlugin("com.github.jpsacha" % "sbt-install4j" % "1.3.1")
```

### build.sbt
Sample use, add following to your `build.sbt`:

```scala
exportJars := true
```
This will export dependent JARs that will be copied to the installer

```scala
enablePlugins(SBTInstall4J)
install4jProjectFile := "installer/example.install4j"
```

## Available Tasks and Settings

SBT tasks provided by  `sbt-install4j` plugin:

* `install4j` : Task - 
  Builds Install4J project. Simple usage from SBT REPL
  ```
  sbt> install4j
  ```
  It will build project defined in by setting `install4jProjectFile`
  
  It can take optional arguments that are passed by to the Install4J compiler. 
  Refer to `install4jc` documentation in [Install4J Help](https://www.ej-technologies.com/resources/install4j/help/doc/#install4j.cli) for list of supported command line options. You can print option summary from SBT REPL using
  ```
  sbt> install4j --help
  ```
  Example of only building `windows` media type
  ```
  sbt> install4j -m windows
  ```

* `install4jCopyDependedJars` : Task - 
  Copies project dependencies to directory `install4jDependedJarsDir`
  

SBT settings provided by  `sbt-install4j` plugin:

* `install4jCopyDependedJarsExclusions` : Seq[String] - 
  List of regex expressions that match files that will be excluded from copying.

* `install4jCopyDependedJarsEnabled` : Boolean -
   if `true` dependent jars will be copies, if `false` they will be not.

* `install4jcFile` : File -  Location of the install4j's command line compiler `install4jc[.exe]`. It can be found in the `bin` directory of the install4j installation. Default can be set with environment variable `INSTALL4JC_FILE`.
  ```scala
  install4jcFile := file("C:/Program Files/install4j8/bin/install4jc.exe")
  ```

* `install4jProjectFile` : String - Relative path to the install4j project file that should be build.

* `install4jDependedJarsDir` : String -
  Location where dependent jars will be copied.
  
* `install4jExtraOptions` : Seq[String] - "Additional command line options passed to the compiler."

* `install4jVerbose` : Boolean -
  Enables verbose mode.

* `install4jRelease` : String -
  Override the application version. 
  Version number components can be alphanumeric and should be separated by dots, dashes or underscores.

* `install4jCompilerVariables` : Map[String, String] -
  Override a compiler variable with a different value.
  In the map, the `key` is variable's name, the `value` is variable's value.
  
* `install4jHomeDir` : File - __Deprecated__. Install4J installation directory. It assumes that Install4J compiler is in subdirectory `bin`. Default can be set with environment variable `INSTALL4J_HOME`. This option is deprecated, use environment variable `INSTALL4JC_FILE` or setting `install4jcFile` instead.
 
## Determining location of Install4J compiler

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

You can set the environment variable when starting SBT using `-D` option, for instance:
```cmd
$ sbt -DINSTALL4JC_FILE="C:/Program Files/install4j8/bin/install4jc.exe"
```

## Tips & Tricks

To see debugging information set SBT logging level to `debug`:
```cmd
sbt> debug
```
Look in the log for lines prefixed with `[debug] [sbt-install4j]`.


## License

sbt-install4j is licensed under [Apache 2.0 license][Apache2].




[Install4J]: https://www.ej-technologies.com/products/install4j/overview.html
[SBT]: http://www.scala-sbt.org/
[Apache2]: https://www.apache.org/licenses/LICENSE-2.0.html
