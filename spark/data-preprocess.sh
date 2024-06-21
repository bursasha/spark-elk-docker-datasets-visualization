#!/bin/bash

SCRIPT_FILE=/opt/bitnami/spark/server-script/preprocessDatasets.scala
SOURCE_DATASET_FILE=./dataset/processed/*.csv
TARGET_DATASET_FILE=../elk/logstash/data/dataset-prices-cataclysms-crimes-1975-2020.csv

###

echo -e "\n *** Starting datasets preprocessing... *** \n"

###

echo -e "\n *** Launching Apache Spark... *** \n"

docker-compose up -d

echo -e "\n *** Launched Apache Spark. *** \n"

###

echo -e "\n *** Starting datasets preprocessing Scala script... *** \n"

docker exec spark bash -c "spark-shell -i $SCRIPT_FILE > /dev/null 2>&1"

echo -e "\n *** Finished datasets preprocessing Scala script. *** \n"

###

docker-compose down

echo -e "\n *** Finished datasets preprocessing successfully! *** \n"

###

echo -e "\n *** Copying preprocessed dataset to Elastic directory... *** \n"

mkdir ../elk/logstash/data
cp $SOURCE_DATASET_FILE $TARGET_DATASET_FILE

echo -e "\n *** Copying preprocessed dataset finished successfully! *** \n"
