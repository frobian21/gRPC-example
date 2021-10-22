/*
 * Copyright 2021 HM Revenue & Customs
 *
 */

package routers

import akka.NotUsed
import akka.stream.scaladsl.Source

class PrimeNumberCalculator {

  def generatePrimes(limit: Int): Source[Int, NotUsed] =
    if (limit < 2) { Source.single(0) } else { Source(primes).takeWhile(_ <= limit) }

  //code for generating primes from stackOverflow ->
  private val primes: Stream[Int] = 2 #:: Stream.from(3, 2).filter(isPrime)

  private def isPrime(n: Int): Boolean = primes.takeWhile(p => p * p <= n).forall(n % _ != 0)

}
