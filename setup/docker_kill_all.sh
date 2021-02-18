#!/bin/bash
docker kill $(docker ps -q) # stop all containers:
docker rm $(docker ps -a -q) # remove all containers
# Comment out by default
#docker rmi $(docker images -q) # remove all docker images
