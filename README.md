sbt-install4j
=============

[![Scala CI](https://github.com/jpsacha/sbt-install4j/actions/workflows/scala.yml/badge.svg)](https://github.com/jpsacha/sbt-install4j/actions/workflows/scala.yml)   [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jpsacha/sbt-install4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.jpsacha/sbt-install4j)


[SBT] plugin for building installers with [Install4J]   ![Install4J](https://www.ej-technologies.com/images/product_banners/install4j_medium.png)


Usage
-----

`sbt-install4j` is available for sbt-1.x.

### project/plugins.sbt

Import `sbt-install4j` plugin to use `install4j` command.

```sbt
addSbtPlugin("com.github.jpsacha" % "sbt-install4j" % "1.4.0")
```

### build.sbt
Sample use, add following to your `build.sbt`:

```sbt
exportJars := true
```
This will export dependent JARs that will be copied to the installer

```sbt
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
  ```sbt
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
C:\Program Files\Install4J10\bin\intall4jc.exe
```

On Mac OS X:
```
/Applications/install4j.app/Contents/Resources/app/bin/install4jc
```

On Linux:
```
/opt/install4j10/bin/install4jc
```

If the Install4J is installed in a different location you can specify location of the compiler using the environment
variable `INSTALL4JC_FILE`, property `INSTALL4JC_FILE`, or SBT setting `install4jcFile`. For multi-platform builds it is
preferred to use the environment variable `INSTALL4JC_FILE`.

You can set the property variable when starting SBT using `-D` option, for instance:
```cmd
$ sbt -DINSTALL4JC_FILE="C:/Program Files/install4j11/bin/install4jc.exe"
```

## Example of a Complete Application

[ScalaFX Ensemble] is an example of an application that is making use of `sbt-install4j` top create an installer with [Install4J] 

## Tips & Tricks

### Compiling against Install4J API
If you use Install4J API in your application and using version other than the default for `sbt-install4j` you will need to set environment variable `INSTALL4J_HOME`. 

For instance, `sbt-install4j` uses Install4J 7 as default, but you have Install4J 9 installed, then set `INSTALL4J_HOME` the installation directory. On Windows that would typically be `C:\Program Files\install4j10`

### Debugging
To see debugging information set SBT logging level to `debug`:
```cmd
sbt> debug
```
Look in the log for lines prefixed with `[debug] [sbt-install4j]`.


## License

sbt-install4j is licensed under [Apache 2.0 license][Apache2].




[Install4J]: https://www.ej-technologies.com/products/install4j/overview.html
[SBT]: http://www.scala-sbt.org/
[ScalaFX Ensemble]: https://github.com/scalafx/scalafx-ensemble
[Apache2]: https://www.apache.org/licenses/LICENSE-2.0.html
