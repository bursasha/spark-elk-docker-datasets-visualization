#!/bin/bash

echo -e "\n *** Starting Elastic Stack initialization... *** \n"

###

cd ./spark
./data-preprocess.sh
cd ..

###

cd ./elk
./data-visualize.sh

###

echo -e "\n *** Finished Elastic Stack initialization successfully! *** \n"
