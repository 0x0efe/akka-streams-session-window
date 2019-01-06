name := "akka-streams-session-window"

version := "0.1"

scalaVersion := "2.12.8"

resolvers += "Apache Releases Repository" at "https://repository.apache.org/content/repositories/releases/"


val akkaVersion      = "2.5.16"
val scalaTestVersion = "3.0.5"

libraryDependencies ++=  Seq(
  "com.typesafe.akka"  %% "akka-stream"         % akkaVersion,
  "com.typesafe.akka"  %% "akka-stream-testkit" % akkaVersion      % Test,
  "org.scalatest"      %% "scalatest"           % scalaTestVersion % Test
)



