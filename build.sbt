enablePlugins(SbtPlugin)

name         := "sbt-install4j"
organization := "com.github.jpsacha"
version      := "1.6.0.1-SNAPSHOT"

homepage             := Some(url("https://github.com/jpsacha/sbt-install4j"))
organizationHomepage := Some(url("https://github.com/jpsacha"))
startYear            := Some(2014)
licenses             := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html"))
description          := "SBT plugin for building installers with Install4J."

scalaVersion := "2.12.18"

// Minimum sbt version supported by the plugin, independent of the sbt version used to build it.
// Also used by scripted tests. sbt 1.9+ is needed to resolve plugins published in Maven-compatible layout,
// which is the only layout published to the Central Portal.
// Keep `scalaVersion` the same as the Scala version used by this sbt version.
pluginCrossBuild / sbtVersion := "1.9.9"

scalacOptions := Seq("-deprecation", "-unchecked", "-Xsource:3")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.20" % "test"

Test / packageBin / publishArtifact := false
Test / packageDoc / publishArtifact := false
Test / packageSrc / publishArtifact := false

// Publishing to Maven Central through the Central Portal, built into sbt 1.11+.
// Release: `sbt publishSigned sonaRelease` (or `sonaUpload`, then press "Publish" in the Central Portal).
// Credentials for `central.sonatype.com` are taken from environment variables SONATYPE_USERNAME and SONATYPE_PASSWORD,
// or from a credentials file, see [https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html]
publishTo := {
  val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
  if (isSnapshot.value) Some("central-snapshots" at centralSnapshots)
  else localStaging.value
}
pomIncludeRepository := { _ => false }
versionScheme        := Some("early-semver")
scmInfo              := Some(
  ScmInfo(url("https://github.com/jpsacha/sbt-install4j"), "scm:git:https://github.com/jpsacha/sbt-install4j.git")
)
developers := List(
  Developer(id = "jpsacha", name = "Jarek Sacha", email = "jpsacha@gmail.com", url = url("https://github.com/jpsacha"))
)

// Scripted tests in `src/sbt-test`, run with `sbt scripted`
scriptedLaunchOpts ++= Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
scriptedBufferLog := false
