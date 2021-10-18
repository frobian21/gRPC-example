package clients

import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import proto._

object PrimeNumberClient {
  def newClient(implicit sys: ActorSystem): PrimeNumberGenerator =
    PrimeNumberGeneratorClient(GrpcClientSettings.fromConfig(PrimeNumberGenerator.name))
}
