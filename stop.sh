#!/bin/bash

echo -e "\n *** Stopping Elastic Stack... *** \n"

###

cd ./elk
docker-compose down

###

cd ./elasticsearch
rm -rf ./data
cd ..

###

cd ./logstash
rm -rf ./data
cd ../..

###

cd ./spark/dataset
rm -rf ./processed

###

echo -e "\n *** Finished Elastic Stack successfully! *** \n"
