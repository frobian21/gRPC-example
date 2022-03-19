package clients

class codingchallenge1 {

  def makePastry(targetLayers: Int): List[LayerOperation] = {
    val initialValue = LayerValue(targetLayers, List.empty[LayerOperation])

    undoFolding(initialValue).previousSteps.reverse.foldRight(List.empty[LayerOperation]) {
      case (Fold(1), Fold(x) :: tail) => Fold(x + 1) :: tail
      case (Add(1), Add(x) :: tail)   => Add(x + 1) :: tail
      case (operation, existingList)  => operation :: existingList
    }
  }

  def makePastryWithBinaryNumber(targetLayers: Int): List[LayerOperation] ={
    //TODO Do with binary number
    // It would've been easier and faster to convert the number to binary and then ran the fold operation to collect the actions. (0 == even => fold, 1 => add).
    // The digit with the highest value in a binary number always has a value of 1 in standard form, so we could either always do an add, or ignore it, and do a fold.
    // This reflects the fact you can go from 1 to 2 layers with a fold or with an add.

    val trimIfPowerOf2: String => String = input => if (input.count(_ == '1') ==1) input.dropRight(1) else input
    trimIfPowerOf2(targetLayers.toBinaryString)
      .map {
      case '0' => Fold(1)
      case '1' => Add(1)
    }.foldRight(List.empty[LayerOperation]) {
      case (Fold(1), Fold(x) :: tail) => Fold(x + 1) :: tail
      case (Add(1), Add(x) :: tail)   => Add(x + 1) :: tail
      case (operation, existingList)  => operation :: existingList
    }
  }

  @scala.annotation.tailrec
  private def undoFolding(layer: LayerValue): LayerValue =
    layer match {
      case LayerValue(1, _) => layer
      case LayerValue(layers, _) if layers % 2 == 0 => undoFolding(undoFold(layer))
      case _ => undoFolding(undoAdd(layer))
    }

  private def undoFold(layerValue: LayerValue): LayerValue =
    layerValue.copy(layerValue.numberOfLayers / 2, layerValue.previousSteps :+ Fold(1))

  private def undoAdd(layerValue: LayerValue): LayerValue =
    layerValue.copy(layerValue.numberOfLayers - 1, layerValue.previousSteps :+ Add(1))
}

case class LayerValue(numberOfLayers: Int, previousSteps: List[LayerOperation]) {
  require(numberOfLayers >= 1)
}

sealed trait LayerOperation {
  val count: Int
}

case class Fold(count: Int) extends LayerOperation

case class Add(count: Int) extends LayerOperation
