// @formatter:off
sbtPlugin := true

name                 := "sbt-install4j"
organization         := "com.github.jpsacha"
version              := "1.4.0"

homepage             := Some(url("http://github.com/jpsacha/sbt-install4j"))
organizationHomepage := Some(url("http://github.com/jpsacha"))
startYear            := Some(2014)
licenses             := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html"))
description          := "SBT plugin for building installers with Install4J."

scalaVersion := "2.12.15"

scalacOptions := Seq("-deprecation", "-unchecked", "-Xsource:3")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % "test"

Test / packageBin / publishArtifact := false
Test / packageDoc / publishArtifact := false
Test / packageSrc / publishArtifact := false

