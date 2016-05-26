name := "sbt-install4j-example"
organization := "ij-plugins.sf.net"
version := "0.1.0"

scalaVersion := "2.11.8"

// Set the prompt (for this build) to include the project id.
shellPrompt in ThisBuild := { state => "sbt:"+Project.extract(state).currentRef.project + "> " }

fork := true
