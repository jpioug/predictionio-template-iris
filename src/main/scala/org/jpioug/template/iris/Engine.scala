package org.jpioug.template.iris

import org.apache.predictionio.controller.{Engine, EngineFactory}

case class Query(attr0: Double,
                 attr1: Double,
                 attr2: Double,
                 attr3: Double)

case class PredictedResult(label: String) extends Serializable

object Engine extends EngineFactory {
  def apply() = {
    new Engine(
      classOf[DataSource],
      classOf[Preparator],
      Map("algo" -> classOf[Algorithm]),
      classOf[Serving])
  }
}
