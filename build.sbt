name := "akka-stream-processor"

version := "0.1"

scalaVersion := "2.12.6" // newer scala version not supported by 'akka-stream-kafka'

lazy val akkaHttpVersion = "10.2.3"
lazy val akkaVersion    = "2.6.12"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.13.4"
    )),
    name := "akka-http-quickstart-scala",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.1.4"         % Test,
      "com.typesafe.akka" %% "akka-stream-kafka" % "0.21.1"
    )
  )

val playVersion = "2.8.1"

libraryDependencies += "com.typesafe.play" %% "play-json" % playVersion


//val AkkaVersion = "2.5.31"
//val AkkaHttpVersion = "10.1.11"
libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-kinesis" % "2.0.2",
//  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
//  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
)