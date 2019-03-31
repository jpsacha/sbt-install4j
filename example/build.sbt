name := "sbt-install4j-example"
organization := "ij-plugins.sf.net"
version := "1.3.0-SHAPSHOT"

scalaVersion := "2.12.8"

// Set the prompt (for this build) to include the project id.
shellPrompt in ThisBuild := { state => "sbt:"+Project.extract(state).currentRef.project + "> " }

fork := true
