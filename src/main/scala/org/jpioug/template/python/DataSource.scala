package org.jpioug.template.python

import org.apache.predictionio.controller.{EmptyActualResult, EmptyEvaluationInfo, PDataSource, Params}
import org.apache.spark.SparkContext
import org.jpioug.template.iris.Query

case class DataSourceParams(appName: String) extends Params

class DataSource(val dsp: DataSourceParams)
  extends PDataSource[TrainingData,
    EmptyEvaluationInfo, Query, EmptyActualResult] {

  override
  def readTraining(sc: SparkContext): TrainingData = {
    new TrainingData()
  }
}

class TrainingData() extends Serializable {}
