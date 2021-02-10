name := "akka-stream-processor"

version := "0.1"

scalaVersion := "2.13.4"

lazy val akkaHttpVersion = "10.2.3"
lazy val AlpakkaVersion = "2.0.1"
lazy val AkkaVersion    = "2.6.12"
lazy val AlpakkaKafkaVersion = "2.0.5"


val scalaVer = "2.13.3"
// #deps


lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.13.4"
    )),
    name := "akka-http-quickstart-scala",
    libraryDependencies ++= Seq(
      "com.lightbend.akka" %% "akka-stream-alpakka-elasticsearch" % AlpakkaVersion,
      "com.typesafe.akka"  %% "akka-stream-kafka"                 % AlpakkaKafkaVersion,
      "com.lightbend.akka" %% "akka-projection-kafka"             % "1.1.0",
      "com.typesafe.akka"  %% "akka-stream"                       % AkkaVersion,
      "com.typesafe.akka"  %% "akka-stream"                       % AkkaVersion,
      "com.typesafe.akka"  %% "akka-actor-typed"                  % AkkaVersion,
      "com.typesafe.akka"  %% "akka-actor"                        % AkkaVersion,
      // for JSON in Scala
      "io.spray" %% "spray-json" % "1.3.5",

      // Logging
      "com.typesafe.akka" %% "akka-slf4j"               % AkkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",

      // #deps
      "com.lightbend.akka" %% "akka-stream-alpakka-elasticsearch" % AlpakkaVersion,
      "com.typesafe.akka" %% "akka-stream-kafka" % AlpakkaKafkaVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
      // for JSON in Scala
      "io.spray" %% "spray-json" % "1.3.5",
      // for JSON in Java
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % "2.10.5",
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.10.5",
      // Logging
      "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      // #deps
      "org.testcontainers" % "elasticsearch" % "1.14.3",
      "org.testcontainers" % "kafka" % "1.14.3"
    )
  )
