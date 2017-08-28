# PredictionIO Iris Template

## Getting Started

### Create App On PIO

```
pio app new --access-key IRIS_TOKEN IrisApp
```

### Import Data

```
python data/import_eventserver.py
```

### Run Jupyter

```
PYSPARK_PYTHON=$PYENV_ROOT/shims/python PYSPARK_DRIVER_PYTHON=$PYENV_ROOT/shims/jupyter PYSPARK_DRIVER_PYTHON_OPTS="notebook" pio-shell --with-pyspark
```

### Run Train on Spark

```
pio train --main-py-file train.py
```

### Deploy App

```
pio deploy
```

### Predict

```
curl -s -H "Content-Type: application/json" -d '{"attr0":5.1,"attr1":3.5,"attr2":1.4,"attr3":0.2}' http://localhost:8000/queries.json
```
