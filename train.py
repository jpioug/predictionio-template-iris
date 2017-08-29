# coding: utf-8

### BEGIN: SETUP ###
import atexit
import platform

import py4j
import sys

import pyspark
from pyspark.context import SparkContext
from pyspark.sql import SparkSession, SQLContext
from pyspark.storagelevel import StorageLevel
from pypio.utils import new_string_array
from pypio.data import PEventStore


SparkContext._ensure_initialized()
try:
    SparkContext._jvm.org.apache.hadoop.hive.conf.HiveConf()
    spark = SparkSession.builder.enableHiveSupport().getOrCreate()
except py4j.protocol.Py4JError:
    spark = SparkSession.builder.getOrCreate()
except TypeError:
    spark = SparkSession.builder.getOrCreate()

sc = spark.sparkContext
sql = spark.sql
atexit.register(lambda: sc.stop())

sqlContext = spark._wrapped
sqlCtx = sqlContext

p_event_store = PEventStore(spark._jsparkSession, sqlContext)


def run_pio_workflow(model):
    template_engine = sc._jvm.org.jpioug.template.python.Engine
    template_engine.modelRef().set(model._to_java())
    main_args = new_string_array(sys.argv, sc._gateway)
    create_workflow = sc._jvm.org.apache.predictionio.workflow.CreateWorkflow
    sc.stop()
    create_workflow.main(main_args)

### END: SETUP ###

# In[ ]:

from pyspark.sql.functions import col
from pyspark.sql.functions import explode
from pyspark.ml.feature import StringIndexer
from pyspark.ml.feature import IndexToString
from pyspark.ml.feature import VectorAssembler
from pyspark.ml.classification import RandomForestClassifier
from pyspark.ml import Pipeline
from pyspark.ml.evaluation import MulticlassClassificationEvaluator


# In[ ]:


event_df = p_event_store.find('IrisApp')


# In[ ]:


def get_field_type(name):
    if name.startswith('attr'):
        return 'double'
    else:
        return 'string'

field_names = (event_df
            .select(explode("fields"))
            .select("key")
            .distinct()
            .rdd.flatMap(lambda x: x)
            .collect())
field_names.sort()
exprs = [col("fields").getItem(k).cast(get_field_type(k)).alias(k) for k in field_names]
data_df = event_df.select(*exprs)


# In[ ]:


(train_df, test_df) = data_df.randomSplit([0.9, 0.1])


# In[ ]:


labelIndexer = StringIndexer(inputCol="target", outputCol="label").fit(train_df)

featureAssembler = VectorAssembler(inputCols=[x for x in field_names if x.startswith('attr')],
                                   outputCol="features")
rf = RandomForestClassifier(labelCol="label", featuresCol="features", numTrees=10)
labelConverter = IndexToString(inputCol="prediction", outputCol="predictedLabel",
                               labels=labelIndexer.labels)
pipeline = Pipeline(stages=[featureAssembler, labelIndexer, rf, labelConverter])


# In[ ]:


model = pipeline.fit(train_df)


# In[ ]:


predict_df = model.transform(test_df)


# In[ ]:


predict_df.select("predictedLabel", "target", "features").show(5)


# In[ ]:


evaluator = MulticlassClassificationEvaluator(
    labelCol="label", predictionCol="prediction", metricName="accuracy")
accuracy = evaluator.evaluate(predict_df)
print("Test Error = %g" % (1.0 - accuracy))


# In[ ]:

run_pio_workflow(model)

