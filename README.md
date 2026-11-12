sbt-install4j
=============

[![Scala CI](https://github.com/jpsacha/sbt-install4j/actions/workflows/scala.yml/badge.svg)](https://github.com/jpsacha/sbt-install4j/actions/workflows/scala.yml)   [![Maven Central](https://img.shields.io/maven-central/v/com.github.jpsacha/sbt-install4j_2.12_1.0)](https://central.sonatype.com/artifact/com.github.jpsacha/sbt-install4j_2.12_1.0)

[SBT] plugin for building installers with
[install4j, the multi-platform installer builder](https://www.ej-technologies.com/install4j).
Works with sbt 1.9+ and sbt 2.

[![install4j multi-platform installer builder](https://www.ej-technologies.com/images/product_banners/install4j_medium.png)](https://www.ej-technologies.com/install4j)

Contents:
* [Quick Start](#quick-start)
* [Tasks](#tasks)
* [Settings](#settings)
* [Location of the install4j Compiler](#location-of-the-install4j-compiler)
* [Tips & Tricks](#tips--tricks)
* [Contributing](#contributing)

Quick Start
-----------

Requirements: sbt 1.9 or newer, or sbt 2 (sbt 2 requires JDK 17 or newer).
For older sbt 1.x use `sbt-install4j` 1.5.0.

Add the SBT plugin to `project/plugins.sbt`:

```sbt
addSbtPlugin("com.github.jpsacha" % "sbt-install4j" % "1.7.0")
```

Enable it in `build.sbt` and point it to your install4j project file:

```sbt
enablePlugins(SBTInstall4J)
install4jProjectFile := "installer/example.install4j"

// sbt 1 only, in sbt 2 this is the default
exportJars := true
```

`exportJars := true` packages classes of your project, and of the projects it depends on, into JARs that are copied
for the installer. Without it only JARs of library dependencies are copied, not your project's classes.

Build the installer:

```
sbt> install4j
```

The jars are copied to `install4jDependedJarsDir`, reference that directory in your install4j project.
For a complete example of an application using `sbt-install4j` see [ScalaFX Ensemble].

Tasks
-----

### `install4j`

This is the main task provided by this plugin. It builds the install4j project defined by `install4jProjectFile`.
It first packages the project (`Compile / packageBin`) and runs `install4jCopyDependedJars`,
then runs the install4j compiler. The task fails if the compiler exits with an error.

Optional arguments are passed to the install4j compiler, see `install4jc` in
[install4j Help](https://www.ej-technologies.com/resources/install4j/help/doc/#install4j.cli) for supported options.
For instance, to print a summary of options, or to build only the `windows` media type:

```
sbt> install4j --help
sbt> install4j -m windows
```

### `install4jCopyDependedJars`

Copies runtime dependencies of the project to `install4jDependedJarsDir`.
Source, Javadoc, and Scaladoc archives are skipped, see `install4jCopyDependedJarsExclusions`.
Non-JAR files and directories are skipped with a warning.

Jars copied by a previous run that are no longer dependencies are removed, other files in that directory are not
touched. To be safe, run `clean` before building a release, it removes the copied jars
when `install4jDependedJarsDir` is under `target` (the default).

`install4jDependedJarsDir` is invoked internally by the `install4j` task.

Settings
--------

| Setting                                                                       | Type                  | Default                                                                      |
|-------------------------------------------------------------------------------|-----------------------|------------------------------------------------------------------------------|
| [`install4jProjectFile`](#install4jprojectfile)                               | `String`              | `installer/installer.install4j`                                              |
| [`install4jcFile`](#install4jcfile)                                           | `File`                | see [compiler location](#location-of-the-install4j-compiler)                 |
| [`install4jDependedJarsDir`](#install4jdependedjarsdir)                       | `String`              | `lib` in `crossTarget`                                                       |
| [`install4jCopyDependedJarsEnabled`](#install4jcopydependedjarsenabled)       | `Boolean`             | `true`                                                                       |
| [`install4jCopyDependedJarsExclusions`](#install4jcopydependedjarsexclusions) | `Seq[String]`         | source, Javadoc, and Scaladoc archives                                       |
| [`install4jRelease`](#install4jrelease)                                       | `String`              | empty, version from the install4j project is used                            |
| [`install4jCompilerVariables`](#install4jcompilervariables)                   | `Map[String, String]` | empty                                                                        |
| [`install4jVerbose`](#install4jverbose)                                       | `Boolean`             | `false`                                                                      |
| [`install4jExtraOptions`](#install4jextraoptions)                             | `Seq[String]`         | empty                                                                        |
| [`install4jHomeDir`](#install4jhomedir)                                       | `File`                | __Deprecated__, see [compiler location](#location-of-the-install4j-compiler) |

### `install4jProjectFile`

Path to the install4j project file to build, relative to the project base directory.

```sbt
install4jProjectFile := "installer/example.install4j"
```

### `install4jcFile`

Location of the install4j command line compiler `install4jc[.exe]`,
in the `bin` directory of the install4j installation.
See [Location of the install4j Compiler](#location-of-the-install4j-compiler) for how the default is determined.

```sbt
install4jcFile := file("C:/Program Files/install4j13/bin/install4jc.exe")
```

### `install4jDependedJarsDir`

Directory where dependent jars are copied. A relative path is resolved against the project base directory.
The default depends on the sbt and Scala versions, for instance,
`target/scala-2.13/lib` in sbt 1, or `target/out/jvm/scala-3.3.8/my-project/lib` in sbt 2.
A fixed location is easier to reference from the install4j project:

```sbt
install4jDependedJarsDir := "target/install4j-lib"
```

### `install4jCopyDependedJarsEnabled`

If `false`, dependent jars are not copied.

### `install4jCopyDependedJarsExclusions`

Regular expressions matching names of files that are not copied.

```sbt
install4jCopyDependedJarsExclusions += """i4jruntime\.jar"""
```

### `install4jRelease`

Overrides the application version (compiler option `--release`).
Version number components can be alphanumeric and should be separated by dots, dashes, or underscores.

```sbt
install4jRelease := version.value
```

### `install4jCompilerVariables`

Overrides values of install4j compiler variables, keyed by variable name.

```sbt
install4jCompilerVariables := Map("appName" -> name.value)
```

### `install4jVerbose`

Enables verbose output of the compiler (`--verbose`).

### `install4jExtraOptions`

Additional command line options passed to the compiler.

### `install4jHomeDir`

__Deprecated__, use [`install4jcFile`](#install4jcfile) or environment variable `INSTALL4JC_FILE` instead.
Install4j installation directory, the compiler is assumed to be in its `bin` subdirectory.

Location of the install4j Compiler
----------------------------------

The location of the install4j compiler is determined in the following order:

1. Setting `install4jcFile`.
2. System property `INSTALL4JC_FILE`.
3. Environment variable `INSTALL4JC_FILE`.
4. `bin/install4jc[.exe]` in the directory given by system property or environment variable `INSTALL4J_HOME`
   (deprecated).
5. Default location for the OS:

   | OS                     | Default location                                             |
   |------------------------|--------------------------------------------------------------|
   | Windows                | `C:\Program Files\install4j13\bin\install4jc.exe`             |
   | macOS                  | `/Applications/install4j.app/Contents/Resources/app/bin/install4jc` |
   | Linux and other        | `/opt/install4j13/bin/install4jc`                             |

For multi-platform builds the environment variable `INSTALL4JC_FILE` is preferred.
A system property can be set when starting sbt:

```
$ sbt -DINSTALL4JC_FILE="C:/Program Files/install4j13/bin/install4jc.exe"
```

In sbt 2, the `sbt` command connects to an sbt server running in the background, by default. The server keeps
the system properties and environment variables it was started with. After changing `INSTALL4JC_FILE` stop the server
with `sbt shutdown`, or run sbt without the background server using `sbt --server`.

Tips & Tricks
-------------

### Compiling against the install4j API

`sbt-install4j` does not add the install4j API to the classpath.
If your application uses the install4j API, add `i4jruntime.jar` from the `resource` directory of the install4j
installation yourself. The installer provides it at runtime, so exclude it from copying:

```sbt
// sbt 1
Compile / unmanagedJars += install4jHomeDir.value / "resource" / "i4jruntime.jar"
// sbt 2
Compile / unmanagedJars += Attributed.blank[xsbti.HashedVirtualFileRef](
  fileConverter.value.toVirtualFile((install4jHomeDir.value / "resource" / "i4jruntime.jar").toPath)
)

install4jCopyDependedJarsExclusions += """i4jruntime\.jar"""
```

If install4j is not in the default location, set the environment variable `INSTALL4J_HOME` to its installation
directory. For instance, with install4j 12 on Windows that is typically `C:\Program Files\install4j12`.

### Debugging

To see debugging information, set the sbt logging level to `debug`
and look in the log for lines prefixed with `[sbt-install4j]`:

```
sbt> debug
sbt> install4j
```

Contributing
------------

Contributions are welcome, see [CONTRIBUTING.md](CONTRIBUTING.md).

License
-------

sbt-install4j is licensed under [Apache 2.0 license][Apache2].


[install4j]: https://www.ej-technologies.com/install4j
[SBT]: https://www.scala-sbt.org/
[ScalaFX Ensemble]: https://github.com/scalafx/scalafx-ensemble
[Apache2]: https://www.apache.org/licenses/LICENSE-2.0.html
