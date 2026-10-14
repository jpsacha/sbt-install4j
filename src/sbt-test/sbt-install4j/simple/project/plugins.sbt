sys.props.get("plugin.version") match {
  case Some(v) => addSbtPlugin("com.github.jpsacha" % "sbt-install4j" % v)
  case _       => sys.error(
      "The system property 'plugin.version' is not defined. Specify it with: -Dplugin.version=..."
    )
}
