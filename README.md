# Subscriber

Library to subscribe topic on MQ and Apache Kafka. The goal is abstract all the steps necessary to connect to each kind of
message broker and subscribe on topic to receive message. Also you can find a client to create the subscribers that you want to register on topic.


## Environment

### Kafka

There is Vagrant file with all steps necessary to install and run kafka. Just execute vagrant command:

    vagrant up

If you want change the number of partitions or create more topics then you can do it editing the below line on install.sh file:

    bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 6 --topic test

Also you can add all the configurations steps that you want, if the vagrant machine is running just run the next command to take your changes.

    vagrant reload provision

### IBM MQ

IBM products need licence to using them, so good luck with that.


## Build

Build uber-jar through maven:

    mvn package


## Test

### Publish

#### Kafka

You can use [kafkameter](https://github.com/BrightTag/kafkameter) that is JMeter plugin to publish message on topic. It works very well.

#### IBM MQ

There is JMeter plugin to connect MQ server through server channel and publish message on topic, it is [mqmeter](https://github.com/JoseLuisSR/mqmeter).
I made it and works good.


### Subscribe

For each message broker, execute Uber-jar with below parameters through console:

#### Kafka

* **Message broker**: KAFKA
* **Number or subscriber**: The number of topic subscribers that you want.
* **Hostname**: Host name or ip address where Kafka Server is running. Vagrant file was configured with ip address 172.28.128.4, of course you can change it.
* **Port**: Port number of the Kafka Server. Vagrant file was configured to use 9092 port from host machine and it match with guest machine port where vagrant server is running.
* **Topic**: Kafka topic name to publish message. The install.sh file contains the commands to create topic 'test'.
* **Consumer group id**: Kafka consumers (subscribers) are part of a consumer group.
* **Message Deserializer**: Kafka class to deserializer the message. It is related with the kafka class used to serialize the message before sending through network.
* **Key Deserializer**: Kafka class to deserializer the key. It is related with the kafka class used to serialize the message before sending through network.

Example

    java -jar subscriber-0.0.1.jar KAFKA 2 172.28.128.4 9092 test device org.apache.kafka.common.serialization.StringDeserializer org.apache.kafka.common.serialization.StringDeserializer

#### IBM MQ

* **Message broker**: MQ
* **Number or subscriber**: The number of topic subscribers that you want.
* **Hostname**: Host name or ip address where MQ Server is running.
* **Port**: Port number of the MQ Server listener.
* **Topic**: MQ topic name to publish message.
* **MQ manager**: MQ Manager name. You can find it through IBM WebSphere MQ Explore or console.
* **MQ channel**: The Server channel name on MQ Server.
* **User id**: The userID to connect to MQ server channel. Put 'null' if you don't need user id to connect to MQ.
* **Password**: The user password to connect to MQ server channel. Put 'null' if you don't need user id and password to connect to MQ.

Example

    java -jar subscriber-0.0.1.jar MQ 3 127.0.0.1 1414 device IOTMQ CLIENT.TO.IOTMQ JoseSR 123456