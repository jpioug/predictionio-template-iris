package org.jpioug.template.iris

import grizzled.slf4j.Logger
import org.apache.predictionio.controller.{P2LAlgorithm, Params}
import org.apache.spark.SparkContext
import org.apache.spark.ml.PipelineModel
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession

case class AlgorithmParams(model_path: String) extends Params

class Algorithm(val ap: AlgorithmParams)
  extends P2LAlgorithm[PreparedData, PipelineModel, Query, PredictedResult] {

  @transient lazy val logger = Logger[this.type]

  def train(sc: SparkContext, data: PreparedData): PipelineModel = {
    PipelineModel.load(ap.model_path)
  }

  def predict(model: PipelineModel, query: Query): PredictedResult = {
    val spark = SparkSession
      .builder
      .appName("IrisApp")
      .getOrCreate()
    val data = Seq((Vectors.dense(query.attr0, query.attr1, query.attr2, query.attr3)))
    val df = spark.createDataset(data).toDF("features")
    val labelDf = model.transform(df)
    PredictedResult(labelDf.first().getAs(0))
  }
}

