package org.jpioug.template.python

import java.util.concurrent.atomic.AtomicReference

import org.apache.predictionio.controller.{Engine, EngineFactory}
import org.apache.spark.ml.PipelineModel
import org.jpioug.template.iris.Algorithm

object Engine extends EngineFactory {

  val modelRef: AtomicReference[PipelineModel] = new AtomicReference[PipelineModel]

  def apply() = {
    new Engine(
      classOf[DataSource],
      classOf[Preparator],
      Map("default" -> classOf[Algorithm]),
      classOf[Serving])
  }
}
