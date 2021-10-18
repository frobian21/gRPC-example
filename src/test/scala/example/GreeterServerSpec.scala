package example

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import akka.stream.scaladsl.{Sink, Source}
import example.{GreeterService, GreeterServiceClient, HelloRequest}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Minutes, Span}

class GreeterClientSpec extends AnyWordSpec with Matchers with ScalaFutures with MockFactory {
  implicit override val patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(3, Minutes), interval = Span(10, Millis))

  implicit protected val system: ActorSystem = ActorSystem("api-test")

  val mockClient: GreeterService = mock[GreeterService]

  "Greeter Client" should {
    "handle server streaming" in {
      val limit = 5
      val response: List[Int] = 1.to(5).toList
      (mockClient.itKeepsReplying _).expects(HelloRequest(limit)).returning(Source(response.map(HelloReply(_))))

      whenReady(GreeterClient.runStreamingReplyExample(limit)(mockClient)){ res =>
        res shouldBe response
      }
    }
  }
}