package org.jpioug.template.iris

import org.apache.predictionio.controller.{Engine, EngineFactory}
import org.jpioug.template.python.{DataSource, Preparator, Serving}

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
      Map("default" -> classOf[Algorithm]),
      classOf[Serving])
  }
}
