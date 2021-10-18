package clients

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import proto._

import scala.concurrent.Future

object PrimeNumberClient {
  implicit val sys        = ActorSystem("PrimeNumberClient")
  lazy val clientSettings = GrpcClientSettings.fromConfig(PrimeNumberGenerator.name)

  def newClient(implicit sys: ActorSystem): PrimeNumberGenerator = PrimeNumberGeneratorClient(clientSettings)

  def runStreamingReplyExample(limit: Int)(client: PrimeNumberGenerator): Future[Seq[Int]] =
    client
      .generatePrimes(PrimeNumberRequest(limit))
      .map(_.primes)
      .runFold(Seq.empty[Int])((seq, int) => seq :+ int)
}
