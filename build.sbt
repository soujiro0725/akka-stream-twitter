lazy val tweetCollector = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(inThisBuild(List(
    organization := "com.soujiro0725",
    name := "akka-stream-twitter",
    version := "0.1.0",
    scalaVersion := "2.12.4"
  ))
  )


val akka_version = "2.4.19"
val twitter_version = "4.0.5"
val scala_test_version = "3.0.4"
val stanford_nlp_version = "3.9.1"
val alpakka_version = "0.18"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akka_version,
  "com.typesafe.akka" %% "akka-testkit" % akka_version % Test,
  "com.typesafe.akka" %% "akka-stream" % akka_version,
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.8" % Test,
  "com.lightbend.akka" %% "akka-stream-alpakka-kinesis" % "0.15.1",
  "com.amazonaws" % "amazon-kinesis-client" % "1.8.8",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "org.scalatest" %% "scalatest" % scala_test_version % "test",
  "org.twitter4j" % "twitter4j-core" % twitter_version,
  "org.twitter4j" % "twitter4j-stream" % twitter_version,

  "edu.stanford.nlp" % "stanford-corenlp" % stanford_nlp_version,
  "edu.stanford.nlp" % "stanford-corenlp" % stanford_nlp_version classifier "models",

  "com.lightbend.akka" %% "akka-stream-alpakka-dynamodb" % alpakka_version
)


maintainer in Docker := "soujiro0725"
dockerBaseImage in Docker := "java:8-jdk-alpine"
dockerExposedPorts in Docker := Seq(8080, 8080)
