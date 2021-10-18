package routers

import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSClient
import play.api.routing.Router
import play.api.Application
import play.grpc.scalatest.ServerGrpcClient
import proto.{PrimeNumberGenerator, PrimeNumberGeneratorClient, PrimeNumberRequest}

class PrimeNumberRouterSpec
    extends PlaySpec
    with GuiceOneServerPerTest
    with ServerGrpcClient
    with ScalaFutures
    with IntegrationPatience {

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
    "work with a gRPC client" in withGrpcClient[PrimeNumberGeneratorClient] { client: PrimeNumberGeneratorClient =>
      implicit val mat: Materializer = app.injector.instanceOf[Materializer]

      val reply = client.generatePrimes(PrimeNumberRequest(5)).runWith(Sink.seq).futureValue.map(_.primes)
      reply mustBe List(1,2,3,4,5)
    }
  }
}
