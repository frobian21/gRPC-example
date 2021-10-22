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

class PrimeNumberRouter @Inject()(mat: Materializer, system: ActorSystem, primeNumberCalculator: PrimeNumberCalculator)
    extends AbstractPrimeNumberGeneratorRouter(system) {

  override def generatePrimes(request: PrimeNumberRequest): Source[PrimeNumberReply, NotUsed] =
    primeNumberCalculator.generatePrimes(request.limit).map(int => PrimeNumberReply(int))
}
