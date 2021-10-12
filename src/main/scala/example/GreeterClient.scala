package example

import akka.{ Done, NotUsed }
import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.scaladsl.Source

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{ Failure, Success }

object GreeterClient {
  def main(args: Array[String]): Unit = {
    // Boot akka
    implicit val sys = ActorSystem("HelloWorldClient")
    implicit val ec = sys.dispatcher

    // Configure the client by code:
    val clientSettings = GrpcClientSettings.connectToServiceAt("127.0.0.1", 8080).withTls(false)

    // Or via application.conf:
    // val clientSettings = GrpcClientSettings.fromConfig(GreeterService.name)

    // Create a client-side stub for the service
    val client: GreeterService = GreeterServiceClient(clientSettings)

    // Run examples for each of the exposed service methods.
    runStreamingReplyExample()

    def runStreamingReplyExample(): Unit = {
      val responseStream = client.itKeepsReplying(HelloRequest(5))
      val done: Future[Done] =
        responseStream.runForeach(reply => println(s"got streaming reply: ${reply.primes}"))

      done.onComplete {
        case Success(_) =>
          println("streamingReply done")
        case Failure(e) =>
          println(s"Error streamingReply: $e")
      }
    }

  }
}
