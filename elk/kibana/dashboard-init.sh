#!/bin/bash

DASHBOARD_FILE=./dashboard/dashboard.ndjson

###

while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' localhost:9400/api/status)" != "200" ]]; 
	do 
		sleep 5; 
	done

###

curl -X POST localhost:9400/api/saved_objects/_import -H "kbn-xsrf: true" --form file=@$DASHBOARD_FILE > /dev/null 2>&1
