#!/bin/bash
bash docker_kill_all.sh
export DEBEZIUM_VERSION=1.4
docker-compose -f docker-compose-mysql.yaml up -d
echo "sleeping for 10s..."; sleep 10s
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @register-mysql.json

# mysql setup
echo "sleeping for 5s..."; sleep 5s
# setup 'courier_order_db.CourierTest' and 'courier_order_db.OrderTest'
mysql --host="0.0.0.0" --port=3306 --user=root --password=debezium < create-schema-and-tables.sql

