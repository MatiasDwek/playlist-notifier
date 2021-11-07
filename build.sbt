enablePlugins(JavaAppPackaging)

import Dependencies._

name := "playlist-notifier"

version := "0.1"

libraryDependencies ++= {
  val akkaVersion = "2.6.3"
  val akkaHttpVersion = "10.1.12"

  // https://mvnrepository.com/artifact/com.typesafe.akka/akka-http-core
  //libraryDependencies += "com.typesafe.akka" %% "akka-http-core" %

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    //"org.scalatest"     %% "scalatest"       % "2.2.0"       % "test"
    scalaTest % Test,
    // https://mvnrepository.com/artifact/se.michaelthelin.spotify/spotify-web-api-java
    "se.michaelthelin.spotify" % "spotify-web-api-java" % "6.4.0"
  )
}

// Assembly settings
mainClass in assembly := Some("com.playlistnotifier.Main")

assemblyJarName in assembly := "playlist-notifier.jar"

assemblyMergeStrategy in assembly := {
  case PathList("reference.conf") => MergeStrategy.concat // https://stackoverflow.com/questions/28365000/no-configuration-setting-found-for-key-akka
  case PathList("META-INF", xs@_*) => MergeStrategy.discard // https://stackoverflow.com/questions/25144484/sbt-assembly-deduplication-found-error
  case x => MergeStrategy.first
}