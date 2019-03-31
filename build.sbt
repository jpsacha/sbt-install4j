// @formatter:off
sbtPlugin := true

name                 := "sbt-install4j"
organization         := "com.github.jpsacha"
version              := "1.2.1-SHAPSHOT"

homepage             := Some(url("http://github.com/jpsacha/sbt-install4j"))
organizationHomepage := Some(url("http://ij-plugins.sf.net"))
startYear            := Some(2014)
licenses             := Seq("GPLv3" -> url("http://www.gnu.org/licenses/gpl.html"))
description          := "SBT plugin for building installers with Install4J."

scalaVersion := "2.12.8"

scalacOptions := Seq("-deprecation", "-unchecked")

publishArtifact in(Test, packageBin) := false
publishArtifact in(Test, packageDoc) := false
publishArtifact in(Test, packageSrc) := false

shellPrompt in ThisBuild := { state => "sbt:"+Project.extract(state).currentRef.project + "> " }

publishTo := version {
  version: String =>
    if (version.contains("-SNAPSHOT"))
      Some("Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
    else
      Some("Sonatype Nexus Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
}.value

