package servers

import akka.NotUsed
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import proto._

class PrimeNumberGeneratorImpl(implicit mat: Materializer) extends PrimeNumberGenerator {
  override def generatePrimes(in: PrimeNumberRequest): Source[PrimeNumberReply, NotUsed] = {
    println(s"sayHello to ${in.limit} with stream of chars...")
    Source(1.to(in.limit).map(int => PrimeNumberReply(int)))
  }

}
