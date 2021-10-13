import sbt._

object Dependencies {
  val AkkaVersion = "2.6.10"
  val AkkaHttpVersion = "10.2.2"

  lazy val compileDependencies = Seq(
    "com.typesafe.akka" %% "akka-discovery" % AkkaVersion
  )
  lazy val testDependencies =  Seq(
  "org.scalatest" %% "scalatest" % "3.2.10" % Test,
  "org.scalacheck" %% "scalacheck" % "1.15.2" % Test,
  "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion % Test,
  "org.scalamock" %% "scalamock" % "5.1.0" % Test
  )
}
