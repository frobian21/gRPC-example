/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package routers

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import com.google.inject.Inject
import proto.{AbstractPrimeNumberGeneratorRouter, PrimeNumberReply, PrimeNumberRequest}

class PrimeNumberRouter @Inject()(mat: Materializer, system: ActorSystem)
    extends AbstractPrimeNumberGeneratorRouter(system) {

  override def generatePrimes(in: PrimeNumberRequest): Source[PrimeNumberReply, NotUsed] = {
    println(s"sayHello to ${in.limit} with stream of chars...")
    Source(1.to(in.limit).map(int => PrimeNumberReply(int)))
  }
}
