package example

import akka.{ Done, NotUsed }
import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.scaladsl.{Sink, Source}


import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{ Failure, Success }

object GreeterClient {
  implicit val sys = ActorSystem("HelloWorldClient")
  lazy val clientSettings = GrpcClientSettings.fromConfig(GreeterService.name)

  def newClient(implicit sys: ActorSystem): GreeterService = GreeterServiceClient(clientSettings)

  def runStreamingReplyExample(limit: Int)(client: GreeterService): Future[Seq[Int]] =
    client
      .itKeepsReplying(HelloRequest(limit))
      .map(_.primes)
      .runFold(Seq.empty[Int])((seq, int) => seq :+ int)
}
