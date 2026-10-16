sbt-install4j
=============

[![Scala CI](https://github.com/jpsacha/sbt-install4j/actions/workflows/scala.yml/badge.svg)](https://github.com/jpsacha/sbt-install4j/actions/workflows/scala.yml)   [![Maven Central](https://img.shields.io/maven-central/v/com.github.jpsacha/sbt-install4j_2.12_1.0)](https://central.sonatype.com/artifact/com.github.jpsacha/sbt-install4j_2.12_1.0)


[SBT] plugin for building installers with
[install4j, the multi-platform installer builder](https://www.ej-technologies.com/install4j).

[![install4j multi-platform installer builder](https://www.ej-technologies.com/images/product_banners/install4j_medium.png)](https://www.ej-technologies.com/install4j)


Usage
-----

`sbt-install4j` is available for sbt 1.9 or newer and for sbt 2 (sbt 2 requires JDK 17 or newer).
For older sbt 1.x use `sbt-install4j` 1.5.0.

### project/plugins.sbt

Import `sbt-install4j` plugin to use `install4j` task.

```sbt
addSbtPlugin("com.github.jpsacha" % "sbt-install4j" % "1.7.0")
```

### build.sbt
Sample use: add the following to your `build.sbt`:

```sbt
enablePlugins(SBTInstall4J)
install4jProjectFile := "installer/example.install4j"
```

In sbt 1 also add:
```sbt
exportJars := true
```
This packages classes of your project, and of projects it depends on, into JARs that are copied to the installer.
Without it your project's classes are not copied, only JARs of library dependencies.
In sbt 2 `exportJars` is `true` by default.

## Available Tasks and Settings

SBT tasks provided by  `sbt-install4j` plugin:

* `install4j` : Task - 
  Builds Install4J project. Simple usage from SBT REPL
  ```
  sbt> install4j
  ```
  It will build project defined by setting `install4jProjectFile`.
  Before running the compiler, it packages the project (`Compile / packageBin`) and runs `install4jCopyDependedJars`.
  The task fails if the install4j compiler exits with an error.
  
  It can take optional arguments that are passed to the Install4J compiler. 
  Refer to `install4jc` documentation in [install4j Help](https://www.ej-technologies.com/resources/install4j/help/doc/#install4j.cli) for list of supported command line options. You can print option summary from SBT REPL using
  ```
  sbt> install4j --help
  ```
  Example of only building `windows` media type
  ```
  sbt> install4j -m windows
  ```

* `install4jCopyDependedJars` : Task - 
  Copies project dependencies to directory `install4jDependedJarsDir`.
  Jars copied by a previous run that are no longer dependencies are removed. 
  Other files in that directory are not touched.
  To be safe, run `clean` before building a release.
  It removes all copied jars when `install4jDependedJarsDir` is under `target` (the default).
  

SBT settings provided by  `sbt-install4j` plugin:

* `install4jCopyDependedJarsExclusions` : Seq[String] - 
  List of regex expressions that match files that will be excluded from copying.

* `install4jCopyDependedJarsEnabled` : Boolean -
   if `true` dependent jars will be copied, if `false` they will be not.

* `install4jcFile` : File -  Location of the install4j's command line compiler `install4jc[.exe]`. It can be found in the `bin` directory of the install4j installation. Default can be set with environment variable `INSTALL4JC_FILE`.
  ```sbt
  install4jcFile := file("C:/Program Files/install4j13/bin/install4jc.exe")
  ```

* `install4jProjectFile` : String - Relative path to the install4j project file that should be built.
  Default is `installer/installer.install4j`.

* `install4jDependedJarsDir` : String -
  Location where dependent jars will be copied.
  Relative path is resolved against the project base directory. Default is `lib` in `crossTarget`,
  for instance, `target/scala-2.13/lib` in sbt 1 or `target/out/jvm/scala-3.3.8/my-project/lib` in sbt 2.
  Setting a fixed location makes it easier to reference from the install4j project, and keeps it the same 
  between sbt versions and when the Scala version changes, for instance:
  ```sbt
  install4jDependedJarsDir := "target/install4j-lib"
  ```
  
* `install4jExtraOptions` : Seq[String] - Additional command line options passed to the compiler.

* `install4jVerbose` : Boolean -
  Enables verbose mode.

* `install4jRelease` : String -
  Override the application version. 
  Version number components can be alphanumeric and should be separated by dots, dashes or underscores.

* `install4jCompilerVariables` : Map[String, String] -
  Override a compiler variable with a different value.
  In the map, the `key` is variable's name, the `value` is variable's value.
  
* `install4jHomeDir` : File - __Deprecated__. install4j installation directory. 
   It assumes that install4j compiler is in subdirectory `bin`. 
   Default can be set with environment variable or system property `INSTALL4J_HOME`. 
   This option is deprecated, use environment variable `INSTALL4JC_FILE` or setting `install4jcFile` instead.
 
## Determining location of install4j compiler

The `sbt-install4j` executes install4j compiler. 
It needs to know its location. 
It attempts to determine the location based on the OS used. 
On Windows it is assumed to be:
```
C:\Program Files\install4j13\bin\install4jc.exe
```

On macOS:
```
/Applications/install4j.app/Contents/Resources/app/bin/install4jc
```

On Linux and other systems:
```
/opt/install4j13/bin/install4jc
```

If the install4j is installed in a different location you can specify location of the compiler using the environment
variable `INSTALL4JC_FILE`, system property `INSTALL4JC_FILE`, or SBT setting `install4jcFile`. 
The system property takes precedence over the environment variable. For multi-platform builds it is
preferred to use the environment variable `INSTALL4JC_FILE`.

You can set the system property when starting SBT using `-D` option, for instance:
```cmd
$ sbt -DINSTALL4JC_FILE="C:/Program Files/install4j13/bin/install4jc.exe"
```

In sbt 2, `sbt` command connects to an sbt server running in the background, by default. The server keeps the system 
properties and environment variables it was started with. After changing `INSTALL4JC_FILE` stop the server with 
`sbt shutdown`, or run sbt in the foreground, without the background server, using `sbt --server`.

## Example of a Complete Application

[ScalaFX Ensemble] is an example of an application that is making use of `sbt-install4j` to create an installer with [install4j] 

## Tips & Tricks

### Compiling against install4j API
`sbt-install4j` does not add the install4j API to the classpath. 
If you use the install4j API in your application, 
add `i4jruntime.jar` from the `resource` directory of the install4j installation yourself. 
The installer provides it at runtime, so exclude it from copying to the installer:
```sbt
// sbt 1
Compile / unmanagedJars += install4jHomeDir.value / "resource" / "i4jruntime.jar"
// sbt 2
Compile / unmanagedJars += Attributed.blank[xsbti.HashedVirtualFileRef](
  fileConverter.value.toVirtualFile((install4jHomeDir.value / "resource" / "i4jruntime.jar").toPath)
)

install4jCopyDependedJarsExclusions += """i4jruntime\.jar"""
```
If your install4j is not in the default location, set environment variable `INSTALL4J_HOME` to its installation 
directory. 
For instance, `sbt-install4j` uses install4j 13 as default, if you have install4j 12 installed, on Windows 
that would typically be `C:\Program Files\install4j12`.

### Debugging
To see debugging information set SBT logging level to `debug`:
```cmd
sbt> debug
```
Look in the log for lines prefixed with `[debug] [sbt-install4j]`.


## License

sbt-install4j is licensed under [Apache 2.0 license][Apache2].




[install4j]: https://www.ej-technologies.com/install4j
[SBT]: http://www.scala-sbt.org/
[ScalaFX Ensemble]: https://github.com/scalafx/scalafx-ensemble
[Apache2]: https://www.apache.org/licenses/LICENSE-2.0.html
