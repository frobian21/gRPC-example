/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package routers

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PrimeNumberCalculatorSpec extends AnyWordSpec with Matchers {

  val service = new PrimeNumberCalculator()

  implicit val sys = ActorSystem("MyTest")
  implicit val mat = Materializer(sys)

  "generatePrimes" should {
    "return a single 0" when {
      "limit is < 2" in {
        val badValues = List(-100, 0, 1)

        badValues.map { limit =>
          service.generatePrimes(limit).runWith(Sink.seq).futureValue shouldBe Seq(0)
        }
      }
    }
    "return primes up to limit" when {
      "limit is >=2" in {
        service.generatePrimes(2).runWith(Sink.seq).futureValue  shouldBe Seq(2)
        service.generatePrimes(5).runWith(Sink.seq).futureValue  shouldBe Seq(2, 3, 5)
        service.generatePrimes(9).runWith(Sink.seq).futureValue  shouldBe Seq(2, 3, 5, 7)
        service.generatePrimes(17).runWith(Sink.seq).futureValue shouldBe Seq(2, 3, 5, 7, 11, 13, 17)
        service.generatePrimes(21).runWith(Sink.seq).futureValue shouldBe Seq(2, 3, 5, 7, 11, 13, 17, 19)
        service.generatePrimes(7919).runWith(Sink.seq).futureValue.size shouldBe 1000
        service.generatePrimes(104729).runWith(Sink.seq).futureValue.size shouldBe 10000
//        service.generatePrimes(1299709).runWith(Sink.seq).futureValue.size shouldBe 100000
      }
    }
  }

}
