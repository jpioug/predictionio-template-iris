#!/bin/bash

APP_NAME=IrisApp
ACCESS_KEY=IRIS_TOKEN
PYTHON_CMD=/opt/conda/bin/python
IMPORT_PYFILE=data/import_eventserver.py
PIO_CMD=/opt/predictionio/bin/pio
PIO_BUILD_ARGS="--verbose"
PIO_TRAIN_ARGS="--main-py-file train.py"
PIO_DEPLOY_ARGS=

run_cmd() {
  echo "[PIO-SETUP] User: $PIO_USER Command: $@"
  if [ x"$PIO_USER" = "xroot" ] ; then
    $@
  else
    sudo -i -u $PIO_USER $@
  fi
}

create_app() {
  run_cmd $PIO_CMD app new --access-key $ACCESS_KEY $APP_NAME
  run_cmd $PIO_CMD app list
}

delete_app() {
  run_cmd $PIO_CMD app delete -f $APP_NAME
}

import_data() {
  run_cmd $PYTHON_CMD $IMPORT_PYFILE --access-key $ACCESS_KEY
}

build() {
  run_cmd $PIO_CMD build $PIO_BUILD_ARGS
}

train() {
  run_cmd $PIO_CMD train $PIO_TRAIN_ARGS
}

deploy() {
  run_cmd $PIO_CMD deploy $PIO_DEPLOY_ARGS
}

run_jupyter() {
  /opt/conda/bin/pip install jupyter
  TMP_FILE=/tmp/run_jupyter.$$
  cat EOS > $TMP_FILE
#!/bin/bash
PYSPARK_PYTHON=/opt/conda/bin/python
PYSPARK_DRIVER_PYTHON=/opt/conda/bin/jupyter
PYSPARK_DRIVER_PYTHON_OPTS="notebook --ip=0.0.0.0"
/opt/predictionio/bin/pio-shell --with-pyspark
EOS
  run_cmd /bin/bash $TMP_FILE
}


case "$PIO_MODE" in
  jupyter)
    run_jupyter
    ;;
  create-app)
    create_app
    ;;
  delete-app)
    create_app
    ;;
  import-data)
    import_data
    ;;
  build)
    build
    ;;
  train-only)
    train
    ;;
  train)
    create_app
    import_data
    build
    train
    ;;
  deploy)
    deploy
    ;;
  *)
    echo "Usage: $SCRIPTNAME {create-app|delete-app|import-data|build|train|train-only|deploy|jupyter}" >&2
    exit 3
    ;;
esac
