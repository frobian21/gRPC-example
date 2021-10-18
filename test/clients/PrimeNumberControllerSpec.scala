/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package clients

import akka.actor.ActorSystem
import akka.stream.scaladsl.Source
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Minutes, Span}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers, Injecting}
import proto.{PrimeNumberGenerator, PrimeNumberReply, PrimeNumberRequest}

import scala.concurrent.ExecutionContext

class PrimeNumberControllerSpec
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with MockFactory
    with GuiceOneAppPerSuite
    with Injecting {
  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = Span(3, Minutes), interval = Span(10, Millis))

  implicit protected val system: ActorSystem  = ActorSystem("api-test")
  implicit protected val ec: ExecutionContext = system.dispatcher

  val mockServer: PrimeNumberGenerator = mock[PrimeNumberGenerator]

  val controller = new PrimeNumberController(system, Helpers.stubControllerComponents()) {
    override lazy val client: PrimeNumberGenerator = mockServer
  }

  val fakeRequest = FakeRequest()

  "PrimeNumberClient" should {
    "handle server streaming" in {
      val limit               = 5
      val response: List[Int] = 1.to(5).toList
      (mockServer.generatePrimes _)
        .expects(PrimeNumberRequest(limit))
        .returning(Source(response.map(PrimeNumberReply(_))))

      val result = controller.getPrimes(limit)(fakeRequest)

      status(result) shouldBe OK
      contentAsString(result) shouldBe "1,2,3,4,5"
    }
  }

}
