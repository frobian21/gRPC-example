resolvers += Resolver.bintrayRepo("playframework", "maven")

addSbtPlugin("com.lightbend.akka.grpc" % "sbt-akka-grpc" % "1.0.2")

addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.16")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.8")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.8")

libraryDependencies += "com.lightbend.play" %% "play-grpc-generators" % "0.9.1"
