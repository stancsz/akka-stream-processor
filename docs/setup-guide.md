# Setup Guide

To set up development environment locally:

## using the pre-written script:
run `setup/setup_script.sh`

## manually

```
# pull yaml & config files
wget https://raw.githubusercontent.com/debezium/debezium-examples/master/tutorial/docker-compose-mysql.yaml

wget https://raw.githubusercontent.com/debezium/debezium-examples/master/tutorial/register-mysql.json
```

```
# Start the topology as defined in https://debezium.io/docs/tutorial/
export DEBEZIUM_VERSION=1.4
docker-compose -f docker-compose-mysql.yaml up

# Start MySQL connector
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @register-mysql.json

# Consume messages from a Debezium topic
docker-compose -f docker-compose-mysql.yaml exec kafka /kafka/bin/kafka-console-consumer.sh \
    --bootstrap-server kafka:9092 \
    --from-beginning \
    --property print.key=true \
    --topic dbserver1.inventory.customers

# Modify records in the database via MySQL client
docker-compose -f docker-compose-mysql.yaml exec mysql bash -c 'mysql -u $MYSQL_USER -p$MYSQL_PASSWORD inventory'

# Shut down the cluster
docker-compose -f docker-compose-mysql.yaml down
```

[debezium tutorial](https://github.com/debezium/debezium-examples/tree/master/tutorial)



once setup successful, debezium will be streaming kafka topics as shown below:

![demo](setup-guide.assets/demo.gif)



after running the consumer main, akka is able to track payloads from the upstream mysql activities

![akka-read-from-kafka-poc](setup-guide.assets/akka-read-from-kafka-poc.gif)