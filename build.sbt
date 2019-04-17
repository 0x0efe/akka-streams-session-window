
val projectName    = "akka-stream-session-window"

crossScalaVersions := Seq("2.12.6", "2.11.12")

val akkaVersion      = "2.5.16"
val scalaTestVersion = "3.0.5"


lazy val commonSettings = Seq(
  organization := "io.github.efekahraman",
  resolvers ++= Seq(
    "Apache Releases Repository" at "https://repository.apache.org/content/repositories/releases/"
  ),
  credentials += Credentials(Path.userHome / ".sbt" / ".credentials"),
  libraryDependencies ++= Seq(
    "com.typesafe.akka"  %% "akka-stream"         % akkaVersion
  )
)


def packageName(module: String): String = List(projectName, module).mkString("-")

lazy val core = (project in file("core"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.akka"  %% "akka-stream-testkit" % akkaVersion      % Test,
      "org.scalatest"      %% "scalatest"           % scalaTestVersion % Test
    ),
    name    := packageName("core")
  )

lazy val root = (project in file("."))
  .settings(
    name      := projectName,
    publishTo := sonatypePublishTo.value
  ).aggregate(core)

