name := "akka-stream-processor"

version := "0.1"

scalaVersion := "2.12.6" // newer scala version not supported by 'akka-stream-kafka'

lazy val akkaHttpVersion = "10.2.3"
lazy val akkaVersion    = "2.6.12"

// should match the dependency in grpc-netty
lazy val grpcVersion = "1.30.2"
lazy val nettyTcnativeVersion = "2.0.29.Final"
lazy val nettyVersion = "4.1.48.Final"

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
      "com.typesafe.akka" %% "akka-stream-kafka" % "0.21.1",

      "io.netty" % "netty-all" % "4.1.30.Final" % "provided", //necessary in fixing the spark io error
      "io.netty" % "netty-all" % nettyVersion % "provided",
      "io.netty" % "netty-buffer" % nettyVersion,
      "io.netty" % "netty-common" % nettyVersion,
)
  )

val playVersion = "2.8.1"

libraryDependencies += "com.typesafe.play" %% "play-json" % playVersion


libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-kinesis" % "2.0.2",
)

libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "2.0.2",
//  "com.typesafe.akka" %% "akka-stream" % AkkaVersion
)


val AkkaVersion = "2.5.31"
libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-file" % "2.0.2",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion
)

libraryDependencies += "com.google.cloud.spark" %% "spark-bigquery-with-dependencies" % "0.19.1"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.1.1"
