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
