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

