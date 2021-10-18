package clients

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.mvc.{AbstractController, Action, AnyContent, BaseController, ControllerComponents, InjectedController}
import proto.{PrimeNumberGenerator, PrimeNumberRequest}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PrimeNumberController @Inject()(actorSystem: ActorSystem, cc: ControllerComponents)(
  implicit ec: ExecutionContext,
  mat: Materializer)
    extends AbstractController(cc) {

  lazy val client: PrimeNumberGenerator = PrimeNumberClient.newClient(actorSystem)

  def getPrimes(limit: Int): Action[AnyContent] = Action.async {
    if (limit > 0)
      client
        .generatePrimes(PrimeNumberRequest(limit))
        .runFold(Seq.empty[Int])((seq, int) => seq :+ int.primes)
        .map(_.mkString(","))
        .map(Ok(_))
    else Future.successful(BadRequest)
  }
}
