package clients

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class codingChallenge1Spec extends AnyWordSpec with Matchers {
  val service = new codingchallenge1
  "makePastry" should {
    "fold" when {
      "2 is the target" in {
        service.makePastry(2) shouldBe List(Fold(1))
      }
    }
    "return the steps required to reach the target layers" when {
      "no duplicated entries" in {
        //(1), 2, 3, 6
        service.makePastry(6) shouldBe List(Fold(1), Add(1), Fold(1))
      }

      "duplicated entries" in {
        //(1), 2, 3, 6, 12
        service.makePastry(12) shouldBe List(Fold(1), Add(1), Fold(2))

        //(1), 2, 4, 8, 9
        service.makePastry(9) shouldBe List(Fold(3), Add(1))
      }
    }
  }
}
