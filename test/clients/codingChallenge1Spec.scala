package clients

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class codingChallenge1Spec extends AnyWordSpec with Matchers {
  val service = new codingchallenge1
  "makePastry" should {
    "return the steps required to reach the target layers" in {

      //(1), 2, 3, 6
      List(List(Add(2), Fold(1)), List(Fold(1), Add(1), Fold(1))) should contain(service.makePastry(6))
    }
  }
}
