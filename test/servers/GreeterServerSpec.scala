package servers

import akka.actor.ActorSystem
import akka.stream.scaladsl.Source
import clients.GreeterClient
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Minutes, Span}
import org.scalatest.wordspec.AnyWordSpec
import proto._

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