# PredictionIO Iris Template

## Requirement

* Spark 2.x

## Getting Started

### Create App on PIO

Create IrisApp application on PredictionIO:

```
pio app new --access-key IRIS_TOKEN IrisApp
```

### Import Data

Import iris data from [iris.data](https://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data).

```
python data/import_eventserver.py
```

### Build Template

Build this template:

```
pio build
```

### Run Jupyter

Launch Jupyter notebook and open eda.ipynb. (or you can create a new notebook to analyze data)

```
PYSPARK_PYTHON=$PYENV_ROOT/shims/python PYSPARK_DRIVER_PYTHON=$PYENV_ROOT/shims/jupyter PYSPARK_DRIVER_PYTHON_OPTS="notebook" pio-shell --with-pyspark
```

### Run Train on Spark

Download Python code from eda.ipynb and put it to train.py.
To execute it on Spark, run `pio train` with --main-py-file option.

```
pio train --main-py-file train.py
```

### Deploy App

Run PredictionIO API server:

```
pio deploy
```

### Predict

Check predictions from deployed model:

```
curl -s -H "Content-Type: application/json" -d '{"attr0":5.1,"attr1":3.5,"attr2":1.4,"attr3":0.2}' http://localhost:8000/queries.json
```

## Run on Docker

[PredictionIO Setup](https://github.com/jpioug/predictionio-setup) dockernizes PredictionIO Template project easily and provides All-In-One environment for train and deploy processes.

### Run Train Steps on Docker

`train` mode executes create-app, import-data, build and train process.

```
PIO_MODE=train docker-compose up --abort-on-container-exit
```

### Run Deploy Step on Docker

`deploy` mode starts Rest API on Docker container.

```
PIO_MODE=deploy docker-compose up --abort-on-container-exit
```

and then check a prediction:

```
curl -s -H "Content-Type: application/json" -d '{"attr0":5.1,"attr1":3.5,"attr2":1.4,"attr3":0.2}' http://localhost:8000/queries.json
```



