name := """WebApp"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.12",
  "com.typesafe.akka" %% "akka-remote" % "2.4.12"
)

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

libraryDependencies ++= Seq (
  "com.typesafe.scala-logging" % "scala-logging-slf4j_2.10" % "2.1.2" % "compile",
  "org.slf4j" % "slf4j-api" % "1.7.12" % "compile",
  "ch.qos.logback" % "logback-classic" % "1.1.3" % "runtime"
)
