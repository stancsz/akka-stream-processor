#!/bin/bash
docker-compose -f docker-compose-mysql.yaml up -d
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @register-mysql.json

