import play.core.PlayVersion.{akkaHttpVersion, akkaVersion, current}
import sbt._

object Dependencies {
  val playGrpcVersion = "0.9.1"

  lazy val compileDependencies = Seq(
    "com.typesafe.play"  %% "play-guice"           % "2.8.8",
    "com.lightbend.play" %% "play-grpc-runtime"    % playGrpcVersion,
    "com.typesafe.akka"  %% "akka-discovery"       % akkaVersion,
    "com.typesafe.akka"  %% "akka-http"            % akkaHttpVersion,
    "com.typesafe.akka"  %% "akka-http-spray-json" % akkaHttpVersion
  )
  val testDependencies = Seq(
    "com.lightbend.play"     %% "play-grpc-scalatest" % playGrpcVersion % Test,
    "com.lightbend.play"     %% "play-grpc-specs2"    % playGrpcVersion % Test,
    "com.typesafe.play"      %% "play-test"           % current         % Test,
    "org.scalatestplus.play" %% "scalatestplus-play"  % "5.0.0"         % Test,
    "com.typesafe.akka"      %% "akka-stream-testkit" % akkaVersion     % Test,
    "org.scalamock"          %% "scalamock"           % "5.1.0"         % Test
  )

}
