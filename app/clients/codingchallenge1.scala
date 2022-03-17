package clients

class codingchallenge1 {

  //record number of steps
  def makePastry(targetLayers: Int): List[Operations] = {
    val initialValue = LayerValue(targetLayers, List.empty[Operations])

    //TODO error cases
    
    addAndFold(initialValue).steps.reverse
    //TODO collapse
  }

  private def addAndFold(layer: LayerValue): LayerValue = {
    layer match {
      case LayerValue(1, steps) => layer
      case LayerValue(layers, steps) if layers % 2 == 0 => addAndFold(doFold(layer))
      case _ => addAndFold(doAdd(layer))
    }
  }

  private def doFold(layerValue: LayerValue): LayerValue =
    layerValue.copy(layerValue.numberOfLayers / 2, layerValue.steps :+ Fold(1))

  private def doAdd(layerValue: LayerValue): LayerValue =
    layerValue.copy(layerValue.numberOfLayers - 1, layerValue.steps :+ Add(1))
}

case class LayerValue(numberOfLayers: Int, steps: List[Operations])

sealed abstract class Operations(count: Int)

case class Fold(count: Int) extends Operations(count)

case class Add(count: Int) extends Operations(count)
