name := "akka-stream-session-window"
crossScalaVersions := Seq("2.12.6", "2.11.12")
organization := "io.github.efekahraman"
resolvers ++= Seq(
  "Apache Releases Repository" at "https://repository.apache.org/content/repositories/releases/"
)

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

val akkaVersion      = "2.5.16"
val scalaTestVersion = "3.0.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka"  %% "akka-stream"         % akkaVersion      % Provided,
  "com.typesafe.akka"  %% "akka-stream-testkit" % akkaVersion      % Test,
  "org.scalatest"      %% "scalatest"           % scalaTestVersion % Test
)



publishTo := sonatypePublishTo.value

