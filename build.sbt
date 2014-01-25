sbtPlugin := true

name := "sbt-install4j"

organization := "net.sf.ij-plugins"

version := "0.0.2-SNAPSHOT"

description := "SBT plugin for building installers with Install4J."

homepage := Some(url("http://github.com/jpsacha/sbt-install4j"))

organizationHomepage := Some(url("http://ij-plugins.sf.net"))

startYear := Some(2014)

licenses := Seq("GPLv3" -> url("http://www.gnu.org/licenses/gpl.html"))

scalacOptions := Seq("-deprecation", "-unchecked")

publishArtifact in(Compile, packageBin) := true

publishArtifact in(Test, packageBin) := false

publishArtifact in(Compile, packageDoc) := true

publishArtifact in(Compile, packageSrc) := true

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