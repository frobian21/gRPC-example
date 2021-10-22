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
import proto.{PrimeNumberGeneratorClient, PrimeNumberReply, PrimeNumberRequest}

import scala.concurrent.ExecutionContext.Implicits.global

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

  val mockClient: PrimeNumberGeneratorClient = mock[PrimeNumberGeneratorClient]

  val controller = new PrimeNumberController(mockClient, Helpers.stubControllerComponents())

  val fakeRequest = FakeRequest()

  "PrimeNumberClient" should {
    "handle server streaming" in {
      val limit               = 5
      val response: List[Int] = 1.to(5).toList
      (mockClient
        .generatePrimes(_: PrimeNumberRequest))
        .expects(PrimeNumberRequest(limit))
        .returning(Source(response.map(PrimeNumberReply(_))))

      val result = controller.getPrimes(limit)(fakeRequest)

      status(result)          shouldBe OK
      contentAsString(result) shouldBe "1,2,3,4,5"
    }
    "ignore 0 and return empty string" in {
      val limit               = 5
      val response: List[Int] = List.fill(5)(0)
      (mockClient
        .generatePrimes(_: PrimeNumberRequest))
        .expects(PrimeNumberRequest(limit))
        .returning(Source(response.map(PrimeNumberReply(_))))

      val result = controller.getPrimes(limit)(fakeRequest)

      status(result)          shouldBe OK
      contentAsString(result) shouldBe ""
    }
  }

}
