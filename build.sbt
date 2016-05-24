// @formatter:off

name                 := "sbt-install4j"
organization         := "com.github.jpsacha"
version              := "1.1.0-SNAPSHOT"

homepage             := Some(url("http://github.com/jpsacha/sbt-install4j"))
organizationHomepage := Some(url("http://ij-plugins.sf.net"))
startYear            := Some(2014)
licenses             := Seq("GPLv3" -> url("http://www.gnu.org/licenses/gpl.html"))
description          := "SBT plugin for building installers with Install4J."

scalaVersion := "2.10.6"

sbtPlugin := true

scalacOptions := Seq("-deprecation", "-unchecked")

publishArtifact in(Test, packageBin) := false
publishArtifact in(Test, packageDoc) := false
publishArtifact in(Test, packageSrc) := false

//publishMavenStyle := false

publishTo <<= version {
  version: String =>
    if (version.contains("-SNAPSHOT"))
      Some("Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots")
    else
      Some("Sonatype Nexus Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
}

pomExtra :=
  <scm>
    <url>git@github.com:jpsacha/sbt-install4j.git</url>
    <connection>scm:git@github.com:jpsacha/sbt-install4j.git</connection>
  </scm>
    <developers>
      <developer>
        <id>jpsacha</id>
        <name>Jarek Sacha</name>
        <url>https://github.com/jpsacha</url>
      </developer>
    </developers>