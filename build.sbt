name := "gRPC-example"

version := "0.1"

scalaVersion := "2.13.6"

enablePlugins(AkkaGrpcPlugin)

libraryDependencies ++= Dependencies.compileDependencies ++ Dependencies.testDependencies
