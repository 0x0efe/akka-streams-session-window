sonatypeProfileName := "io.github.efekahraman"

publishMavenStyle := true

licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

import xerial.sbt.Sonatype._
sonatypeProjectHosting := Some(GitHubHosting("efekahraman", "akka-streams-session-window", "efekahraman@gmail.com"))

homepage := Some(url("http://ekahraman.me"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/efekahraman/akka-streams-session-window"),
    "scm:git@github.com:efekahraman/akka-streams-session-window.git"
  )
)
developers := List(
  Developer(id="efekahraman", name="Efe Kahraman", email="efekahraman@gmail.com", url=url("http://ekahraman.me"))
)