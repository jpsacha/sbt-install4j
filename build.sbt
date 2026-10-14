enablePlugins(SbtPlugin)

name         := "sbt-install4j"
organization := "com.github.jpsacha"
version      := "1.5.0.1-SNAPSHOT"

homepage             := Some(url("https://github.com/jpsacha/sbt-install4j"))
organizationHomepage := Some(url("https://github.com/jpsacha"))
startYear            := Some(2014)
licenses             := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html"))
description          := "SBT plugin for building installers with Install4J."

scalaVersion := "2.12.17"

// Minimum sbt version supported by the plugin, independent of the sbt version used to build it.
// Also used by scripted tests.
pluginCrossBuild / sbtVersion := "1.8.3"

scalacOptions := Seq("-deprecation", "-unchecked", "-Xsource:3")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.20" % "test"

Test / packageBin / publishArtifact := false
Test / packageDoc / publishArtifact := false
Test / packageSrc / publishArtifact := false

// Scripted tests in `src/sbt-test`, run with `sbt scripted`
scriptedLaunchOpts ++= Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
scriptedBufferLog := false
