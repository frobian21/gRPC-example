package servers

import akka.actor.ActorSystem
import akka.stream.scaladsl.Source
import clients.PrimeNumberClient
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Minutes, Span}
import org.scalatest.wordspec.AnyWordSpec
import proto._

class PrimeNumberClientSpec extends AnyWordSpec with Matchers with ScalaFutures with MockFactory {
  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = Span(3, Minutes), interval = Span(10, Millis))

  implicit protected val system: ActorSystem = ActorSystem("api-test")

  val mockServer: PrimeNumberGenerator = mock[PrimeNumberGenerator]

  "PrimeNumberClient" should {
    "handle server streaming" in {
      val limit               = 5
      val response: List[Int] = 1.to(5).toList
      (mockServer.generatePrimes _)
        .expects(PrimeNumberRequest(limit))
        .returning(Source(response.map(PrimeNumberReply(_))))

      whenReady(PrimeNumberClient.runStreamingReplyExample(limit)(mockServer)) { res =>
        res shouldBe response
      }
    }
  }
}
