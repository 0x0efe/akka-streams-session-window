
val projectName    = "akka-streams-session-window"

organization := "me.ekahraman"

scalaVersion := "2.12.8"


val akkaVersion      = "2.5.16"
val scalaTestVersion = "3.0.5"


lazy val commonSettings = Seq(
  resolvers ++= Seq(
    "Apache Releases Repository" at "https://repository.apache.org/content/repositories/releases/"
  ),
  credentials += Credentials(Path.userHome / ".sbt" / ".credentials"),
  libraryDependencies ++= Seq(
    "com.typesafe.akka"  %% "akka-stream"         % akkaVersion
  )
)

lazy val core = (project in file("core"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.akka"  %% "akka-stream-testkit" % akkaVersion      % Test,
      "org.scalatest"      %% "scalatest"           % scalaTestVersion % Test
    )
  )

lazy val root = (project in file("."))
  .settings(
    name    := projectName
  ).aggregate(core)