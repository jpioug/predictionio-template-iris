package org.jpioug.template.iris

import grizzled.slf4j.Logger
import org.apache.predictionio.controller.{P2LAlgorithm, Params}
import org.apache.spark.SparkContext
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.SparkSession
import org.jpioug.template.python.{ModelState, PreparedData}

case class AlgorithmParams(name: String) extends Params

class Algorithm(val ap: AlgorithmParams)
  extends P2LAlgorithm[PreparedData, PipelineModel, Query, PredictedResult] {

  @transient lazy val logger = Logger[this.type]

  def train(sc: SparkContext, data: PreparedData): PipelineModel = {
// TODO ModelState.get[PipelineModel]
    None
  }

  def predict(model: PipelineModel, query: Query): PredictedResult = {
    val spark = SparkSession
      .builder
      .appName(ap.name)
      .getOrCreate()
    import spark.implicits._
    val data = Seq(
      (query.attr0, query.attr1, query.attr2, query.attr3)
    )
    val df = spark.createDataset(data).toDF("attr0", "attr1", "attr2", "attr3")
    val labelDf = model.transform(df)
    PredictedResult(labelDf.select("predictedLabel").first().getAs(0))
  }
}

