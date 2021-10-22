package routers

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSClient
import play.api.routing.Router
import play.api.Application
import play.grpc.scalatest.ServerGrpcClient
import proto.{PrimeNumberGenerator, PrimeNumberGeneratorClient, PrimeNumberReply, PrimeNumberRequest}

class PrimeNumberRouterSpec
    extends PlaySpec
    with GuiceOneServerPerTest
    with ServerGrpcClient
    with ScalaFutures
    with IntegrationPatience
    with MockFactory {

  override def fakeApplication(): Application =
    GuiceApplicationBuilder().overrides(bind[Router].to[PrimeNumberRouter]).build()

  implicit def ws: WSClient = app.injector.instanceOf(classOf[WSClient])

  "A Play server bound to a gRPC router" must {
    "give a 404 when routing a non-gRPC request" in {
      val result = wsUrl("/").get.futureValue
      result.status mustBe 404
    }
    "give an Ok header (and hopefully a not implemented trailer) when routing a non-existent gRPC method" in {
      val result = wsUrl(s"/${PrimeNumberGenerator.name}/FooBar")
        .addHttpHeaders("Content-Type" -> "application/grpc")
        .get
        .futureValue
      result.status mustBe 200
    }
    "give a 200 when routing an empty request to a gRPC method" in {
      val result = wsUrl(s"/${PrimeNumberGenerator.name}/SayHello")
        .addHttpHeaders("Content-Type" -> "application/grpc")
        .get
        .futureValue
      result.status mustBe 200
    }
    //TODO mock the call to PrimeNumberCalculator here. This feels like a ITTest
    "work with a gRPC client" in withGrpcClient[PrimeNumberGeneratorClient] { client: PrimeNumberGeneratorClient =>
      implicit val mat: Materializer = app.injector.instanceOf[Materializer]

      client.generatePrimes(PrimeNumberRequest(5)).runWith(Sink.seq).futureValue.map(_.primes) mustBe List(2, 3, 5)
      client.generatePrimes(PrimeNumberRequest(1)).runWith(Sink.seq).futureValue.map(_.primes) mustBe List(0)
    }
  }
  "primeNumberRouter" should {
    "call the PrimeNumberCalculator and wrap the results" in {
      val sys            = ActorSystem("MyTest")
      implicit val mat   = Materializer(sys)
      val mockCalculator = mock[PrimeNumberCalculator]
      val stubbedRouter  = new PrimeNumberRouter(mat, sys, mockCalculator)

      val response: List[Int] = 1.to(5).toList
      (mockCalculator.generatePrimes _).expects(5).returning(Source(response))

      stubbedRouter.generatePrimes(PrimeNumberRequest(5)).runWith(Sink.seq).futureValue.map(_.primes) mustBe response
    }
  }
}
