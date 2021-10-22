package clients

import akka.stream.Materializer
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import proto.{PrimeNumberGeneratorClient, PrimeNumberRequest}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PrimeNumberController @Inject()(client: PrimeNumberGeneratorClient, cc: ControllerComponents)(
  implicit ec: ExecutionContext,
  mat: Materializer)
    extends AbstractController(cc) {

  def getPrimes(limit: Int): Action[AnyContent] = Action.async {
    if (limit > 0)
      client
        .generatePrimes(PrimeNumberRequest(limit))
        .map(_.primes)
        .filterNot(_ == 0)
        .runFold(Seq.empty[Int])(_ :+ _)
        .map(seq => Ok(seq.mkString(",")))
    else Future.successful(BadRequest)
  }
}
