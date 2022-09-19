# Setting Up Kafka with docker

## Use this tutorial for Kafka setup with Docker Compose
https://limascloud.com/2022/01/02/docker-compose-kafka-setup-confluent-cloud/
https://www.section.io/engineering-education/what-is-kafka-how-to-build-and-dockerize-a-kafka-cluster/

## Start Zookeeper and Kafka Broker
```bash
$ docker compose up -d
```

# Setting Up Kafka without docker

---
> **NOTE:**  Make sure you are navigated inside the ```kafka/bin``` directory
---
```
cd Downloads/kafka_2.13-3.2.0/bin/
```

## Start Zookeeper
```
./zookeeper-server-start.sh ../config/zookeeper.properties
```
## Start Kafka Broker

---
> **NOTE:** Before starting up kafka broker add the below properties in the server.properties
---

```
listeners=PLAINTEXT://localhost:9092
auto.create.topics.enable=false
```
```
./kafka-server-start.sh ../config/server.properties
```

## Create test topic

```
./kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 4
```
## Create Item topic
```
./kafka-topics.sh --create --topic item-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 4
```

## Create a topic with Replication Factor

```
./kafka-topics.sh --create --topic test-topic-replicated --bootstrap-server localhost:9092 --replication-factor 3 --partitions 3
```

## Create a topic with a name

```
./kafka-topics.sh --create --topic items --bootstrap-server localhost:9092 --replication-factor 3 --partitions 3
```

## Instantiate a Console Producer

### Without Key

```
./kafka-console-producer.sh --broker-list localhost:9092 --topic test-topic
```

### With Key

```
./kafka-console-producer.sh --broker-list localhost:9092 --topic test-topic --property "key.separator=-" --property "parse.key=true"
```
## Instantiate a Console Consumer

### Without Key

```
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --from-beginning
```

### With Key

```
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --from-beginning -property "key.separator= - " --property "print.key=true"
```