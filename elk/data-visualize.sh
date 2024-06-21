#!/bin/bash

echo -e "\n *** Starting ELK Stack... *** \n"

docker-compose up -d

echo -e "\n *** Launched ELK Stack. *** \n"

###

echo -e "\n *** Starting dashboard initialization script... *** \n"

cd ./kibana
./dashboard-init.sh

echo -e "\n *** Finished dashboard initialization script. *** \n"

###

echo -e "\n *** Finished datasets preprocessing successfully! *** \n"
