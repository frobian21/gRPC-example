import sbt.Compile

lazy val commonSettings = Seq(
  name := "gRPC-example",
  version := "0.1",
  scalaVersion := "2.13.6",
  libraryDependencies ++= Dependencies.compileDependencies ++ Dependencies.testDependencies,
  Compile / scalaSource := baseDirectory.value / "app",
  Test / scalaSource := baseDirectory.value / "test"
)

lazy val client = (project in file("."))
  .settings(
    commonSettings
    //stub server
  )
  .enablePlugins(play.sbt.PlayScala, AkkaGrpcPlugin, ScalafmtCorePlugin)

//lazy val server = (project in file("server")).settings(
//  commonSettings
//  //stub client
//)
