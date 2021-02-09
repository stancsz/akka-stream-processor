name := "akka-stream-processor"

version := "0.1"

scalaVersion := "2.13.4"

lazy val akkaHttpVersion = "10.2.3"
lazy val AlpakkaVersion = "2.0.1"
lazy val AkkaVersion    = "2.6.12"
lazy val AlpakkaKafkaVersion = "2.0.5"

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
      "com.typesafe.akka"  %% "akka-actor-typed"                  % AkkaVersion,
      "com.typesafe.akka"  %% "akka-actor"                        % AkkaVersion,
      // for JSON in Scala
      "io.spray" %% "spray-json" % "1.3.5",

      // Logging
      "com.typesafe.akka" %% "akka-slf4j"               % AkkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3"
    )
  )
