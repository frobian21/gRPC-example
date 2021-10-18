import play.grpc.gen.scaladsl.{PlayScalaClientCodeGenerator, PlayScalaServerCodeGenerator}

lazy val commonSettings = Seq(
  name := "gRPC-example",
  version := "0.1",
  scalaVersion := "2.12.15",
  libraryDependencies ++= Dependencies.compileDependencies ++ Dependencies.testDependencies,
  akkaGrpcGeneratedLanguages := Seq(AkkaGrpc.Scala),
  PlayKeys.devSettings ++= Seq(
    "play.server.http.port" -> "disabled",
    "play.server.https.port" -> "9443",
    "play.server.https.keyStore.path" -> "conf/selfsigned.keystore",
  )

)

lazy val client = (project in file("."))
  .enablePlugins(play.sbt.PlayScala, PlayAkkaHttp2Support, AkkaGrpcPlugin, ScalafmtCorePlugin)
  .settings(
    commonSettings,
    akkaGrpcExtraGenerators += PlayScalaClientCodeGenerator,
    akkaGrpcExtraGenerators += PlayScalaServerCodeGenerator
  )

//TODO split the server into a separate module
//lazy val server = (project in file("server")).settings(
//  commonSettings,
//  akkaGrpcExtraGenerators += PlayScalaServerCodeGenerator
//)
