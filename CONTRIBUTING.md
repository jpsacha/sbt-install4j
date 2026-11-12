# Contributing to sbt-install4j

Thank you for your interest in contributing to sbt-install4j! Contributions of all kinds are welcome:
bug reports, fixes, new features, and documentation improvements.

## Ways to Contribute

* **Bug reports, questions, and feature requests** - use the
  [Issue Tracker](https://github.com/jpsacha/sbt-install4j/issues).
  For bugs, include the sbt-install4j, sbt, install4j, and JDK versions, your OS, and the relevant part of your
  `build.sbt`. The sbt log at the `debug` level helps too, see [Debugging](README.md#debugging).
* **Code** - submit a pull request, see [Pull Requests](#pull-requests) below.
  For larger changes, open an issue first, so the approach can be agreed on before you spend time on it.
* **Documentation** - improvements to [README.md](README.md) are welcome.

## Software Required

1. [sbt](https://www.scala-sbt.org/). The sbt launcher picks up the version from `project/build.properties`
   automatically.
2. JDK 17 or newer, needed to build the sbt 2 version of the plugin.
3. [install4j](https://www.ej-technologies.com/install4j) is only needed to build an actual installer,
   for instance from the [example](example) project. Unit and scripted tests do not need it.

The build works on Windows, macOS, and Linux.

## Building from Source

The plugin is cross-built for sbt 1 (Scala 2.12) and sbt 2 (Scala 3).
Use the `+` prefix to run a command for both.

```shell
git clone https://github.com/jpsacha/sbt-install4j.git
cd sbt-install4j
sbt compile          # compile the sbt 1 version
sbt +test            # run unit tests for sbt 1 and sbt 2
sbt +scripted        # run scripted tests for sbt 1 and sbt 2
sbt +publishLocal    # publish both versions to the local repository
```

To try a local build in the [example](example) project, run `sbt +publishLocal`, then set the plugin version in
`example/project/plugin-install4j.sbt` to the version in [build.sbt](build.sbt) (`-SNAPSHOT`).

IntelliJ IDEA (with the Scala plugin) and VS Code (with Metals) can import the project directly from `build.sbt`.

## Project Structure

```
.
├── example             example project using the plugin, with an install4j project file
├── notes               release notes
├── project             sbt build setup and plugins
└── src
    ├── main
    │   ├── scala       code shared by sbt 1 and sbt 2
    │   ├── scala-2.12  code specific to sbt 1
    │   └── scala-3     code specific to sbt 2
    ├── sbt-test        scripted tests
    └── test            unit tests
```

Differences between sbt 1 and sbt 2 APIs are kept in `PluginCompat.scala`,
one version in `scala-2.12` and one in `scala-3`, with the same API.

## Coding Guidelines

* **Cross-building** - the code must work with sbt 1 and sbt 2. Shared sources use syntax accepted by both
  Scala 2.12 and Scala 3 (Scala 2.12 is run with `-Xsource:3`).
* **Formatting** - the code is formatted with [scalafmt](https://scalameta.org/scalafmt/) using `.scalafmt.conf`.
  Format the files you change, for instance in your IDE.
* **License header** - every source file starts with the Apache 2.0 license header. Copy it from an existing file
  when you create a new one.
* **Tests** - add or update tests for your changes. Unit tests are in `src/test` and use
  [ScalaTest](https://www.scalatest.org/). Behavior of the plugin in an sbt build is tested with
  [scripted tests](https://www.scala-sbt.org/1.x/docs/Testing-sbt-plugins.html) in `src/sbt-test`.
* **Documentation** - when you add or change a task or a setting, update [README.md](README.md) and the key
  description in `SBTInstall4J.scala`.

## Pull Requests

1. Fork the repository and create a branch from `main`. Use a separate branch for each feature or bug fix.
2. Make your changes, with tests.
3. Make sure `sbt +test +scripted` passes. CI runs it on JDK 17.
4. Open a pull request against `main`. Reference the related issue, for instance `Fixes #123`.

Keep pull requests focused. Small, independent pull requests are easier to review and merge.

## Source Code Branching Policy

* Development happens on the `main` branch.
* Releases are done on the `release` branch and tagged with the version number, for instance `v.1.6.0`.
* Pull requests are only accepted off a branch created from the `main` branch.

## Releasing (Maintainers)

Prerequisites:

- JDK 17 or newer.
- PGP key for signing artifacts, `gpg` on the `PATH`, see [sbt-pgp](https://github.com/sbt/sbt-pgp).
- Sonatype Central Portal credentials, `SONATYPE_USERNAME` and `SONATYPE_PASSWORD` environment variables,
  or credentials for `central.sonatype.com` in a credentials file.

Steps:

1. Write the release notes, `notes/<version>.md`, and commit them to `main`.
   Use notes of the previous release as a template.
2. Merge `main` into `release`.
3. On `release`, set the release version in `build.sbt`, removing `-SNAPSHOT`, and update the version in
   [README.md](README.md) and in the [example](example) project.
4. Run tests, then publish both versions to Maven Central:
   ```shell
   sbt +test +scripted
   sbt +publishSigned sonaRelease
   ```
   Alternatively, use `sonaUpload` instead of `sonaRelease`, then press "Publish" in the
   [Central Portal](https://central.sonatype.com/publishing/deployments).
   Check that both artifacts, `sbt-install4j_2.12_1.0` and `sbt-install4j_sbt2_3`, are published.
5. Commit the version change ("Mark release version x.y.z"), tag it (`v.x.y.z`), and push `release` and the tag.
6. Merge `release` into `main`, set the next development version (`-SNAPSHOT`) in `build.sbt`, commit
   ("Mark the next development version"), and push `main`.

## Code of Conduct

Please be respectful and constructive in issues and pull requests. We follow the
[Typelevel Code of Conduct](https://typelevel.org/code-of-conduct/).

## License

By contributing to sbt-install4j, you agree that your contributions will be licensed under the
[Apache 2.0 License](LICENSE).
