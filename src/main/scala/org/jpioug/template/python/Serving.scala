package org.jpioug.template.python

import org.apache.predictionio.controller.LServing
import org.jpioug.template.iris.{PredictedResult, Query}

class Serving
  extends LServing[Query, PredictedResult] {

  override
  def serve(query: Query,
    predictedResults: Seq[PredictedResult]): PredictedResult = {
    predictedResults.head
  }
}
