// Configuration for sbt-sonatype plugin

import xerial.sbt.Sonatype.GitHubHosting

sonatypeProfileName := "com.github.jpsacha"
sonatypeProjectHosting := Some(GitHubHosting("jpsacha", "sbt-install4j", "jpsacha@gmail.com"))

publishTo := sonatypePublishTo.value

developers := List(
  Developer(id = "jpsacha", name = "Jarek Sacha", email = "jpsacha@gmail.com", url = url("https://github.com/jpsacha"))
)