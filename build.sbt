// @formatter:off
sbtPlugin := true

name                 := "sbt-install4j"
organization         := "com.github.jpsacha"
version              := "1.3.1"

homepage             := Some(url("http://github.com/jpsacha/sbt-install4j"))
organizationHomepage := Some(url("http://github.com/jpsacha"))
startYear            := Some(2014)
licenses             := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html"))
description          := "SBT plugin for building installers with Install4J."

scalaVersion := "2.12.10"

scalacOptions := Seq("-deprecation", "-unchecked")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

publishArtifact in(Test, packageBin) := false
publishArtifact in(Test, packageDoc) := false
publishArtifact in(Test, packageSrc) := false

shellPrompt in ThisBuild := { state => "sbt:"+Project.extract(state).currentRef.project + "> " }


