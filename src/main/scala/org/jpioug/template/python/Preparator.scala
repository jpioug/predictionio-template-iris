package org.jpioug.template.python

import org.apache.predictionio.controller.PPreparator
import org.apache.spark.SparkContext

class Preparator
  extends PPreparator[TrainingData, PreparedData] {

  def prepare(sc: SparkContext, trainingData: TrainingData): PreparedData = {
    new PreparedData()
  }
}

class PreparedData() extends Serializable
