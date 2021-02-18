#!/bin/bash
# Comment out by default
#docker kill $(docker ps -q) # stop all containers:
#docker rm $(docker ps -a -q) # remove all containers
#docker rmi $(docker images -q) # remove all docker images

export DEBEZIUM_VERSION=1.4

docker-compose -f docker-compose-mysql.yaml up -d

echo "sleeping for 10s..."; sleep 10s

curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @register-mysql.json

