package org.jpioug.template.iris

import org.apache.predictionio.controller.{P2LAlgorithm, Params}
import org.apache.spark.SparkContext
import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.SparkSession
import org.jpioug.template.python.{Engine, PreparedData}

case class AlgorithmParams(name: String) extends Params

case class Query(attr0: Double,
                 attr1: Double,
                 attr2: Double,
                 attr3: Double)

case class PredictedResult(label: String) extends Serializable

class Algorithm(val ap: AlgorithmParams)
  extends P2LAlgorithm[PreparedData, PipelineModel, Query, PredictedResult] {

  def train(sc: SparkContext, data: PreparedData): PipelineModel = {
    Engine.modelRef.get()
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

