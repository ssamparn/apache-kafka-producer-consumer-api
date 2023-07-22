## The intention of this project is to learn
- The internals of Apache Kafka
- Kafka Consumers and Producers using Java

### Udemy Course:
[Course Link](https://www.udemy.com/course/apache-kafka-deep-dive-hands-on-using-javabuilt-in-scripts/)

### Setting Up Kafka with docker ###

### Use this tutorial for Kafka setup with Docker Compose
https://limascloud.com/2022/01/02/docker-compose-kafka-setup-confluent-cloud/
https://www.section.io/engineering-education/what-is-kafka-how-to-build-and-dockerize-a-kafka-cluster/

#### Note: However, the kafka cluster in this project is not build built by docker compose. 

## Start Zookeeper and Kafka Broker
```bash
$ docker compose up -d
```

# Setting Up Kafka without docker

---
> **NOTE:**  Make sure you are navigated inside the ```kafka/bin``` directory
---
```bash
$ cd Downloads/kafka_2.13-3.5.0/bin/
```

## Start Zookeeper
```bash
$ ./zookeeper-server-start.sh ../config/zookeeper.properties
```
## Start Kafka Broker

---
> **NOTE:** Before starting up kafka broker add the below properties in the server.properties
---

```
listeners=PLAINTEXT://localhost:9092
auto.create.topics.enable=false
```

```bash
$ ./kafka-server-start.sh ../config/server.properties
```

## Create test topic

```bash
$ ./kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 4
```
## Create Item topic

```bash
$ ./kafka-topics.sh --create --topic item-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 4
```

## Create a topic with Replication Factor

```bash
$ ./kafka-topics.sh --create --topic test-topic-replicated --bootstrap-server localhost:9092 --replication-factor 3 --partitions 3
```

## Create a topic with a name

```bash
$ ./kafka-topics.sh --create --topic items --bootstrap-server localhost:9092 --replication-factor 3 --partitions 3
```

## Instantiate a Console Producer

### Without Key

```bash
$ ./kafka-console-producer.sh --broker-list localhost:9092 --topic test-topic
```

### With Key

```bash
$ ./kafka-console-producer.sh --broker-list localhost:9092 --topic test-topic --property "key.separator=-" --property "parse.key=true"
```
## Instantiate a Console Consumer

### Without Key

```bash
$ ./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --from-beginning
```

### With Key

```bash
$ ./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --from-beginning -property "key.separator= - " --property "print.key=true"
```