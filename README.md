# Connector Publish & Subscribe.

Using SOLID principles to abstract all the common behaviour between different message broker like Kafka & IBM MQ
and create classes with capabilities to connect like Publisher or Subscriber for one of these components. Also you can use
this like a guide to know how connect to Kafka or MQ and announce or receive message/events.

## Publisher

Publisher interface has the main methods definitions to access, leave and announce an event on Topic. You can use Kafka or MQ
classes to integrate to each one message broker.

## Subscriber

Subscriber interface has the definitions to subscribe, unsubscribe and listen a Topic. Also you can find Kafka and MQ
classes with all the logic to interact with each one message broker.


# Environment

## Kafka

You can use Vagrant to install and execute Kafka on virtual machine. This repository has a Vagrant file with all steps to
install and run Kafka, just execute the below commando on your work area.

    vagrant up

If you want change the number of partitions or create more topics then you can do it editing the below line on install.sh file:

    bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 6 --topic test

Also you can add all the configurations steps that you want, if the vagrant machine is running just run the next command to take your changes.

    vagrant reload provision

## IBM MQ

IBM products need licence to using them, so good luck.


# Build

Build uber-jar through maven:

    mvn package

On /target folder you can find connector-x.x.x.jar file that you could use as library on your project and also you could
execute Publisher or Subscriber client.

# Test

## Publisher

### Kafka

Execute connector-x.x.x.jar with the below arguments:

* **Connector type**: Publisher
* **Message broker**: KAFKA
* **Number of Publishers**: The number of publishers that access Topic.
* **Hostname**: Host name or ip address where Kafka Server is running. Vagrant file was configured with ip address 172.28.128.4, of course you can change it.
* **Port**: Port number of the Kafka Server. Vagrant file was configured to use 9092 port from host machine and it match with guest machine port where vagrant server is running.
* **Topic**: Kafka topic name to publish message. The install.sh file contains the commands to create topic 'test'.
* **Encoding**: Character encoding standard for your message: For EBCDIC put Cp1047. ASCII just put ASCII.
* **Message Serializer**: Kafka class to Serializer the message before send to Kafka.
* **Key Serializer**: Kafka class to Serializer the key before send to Kafka.
* **Topic partition**: Kafka partition number of the topic.
* **Topic key**: To set up identify to events that are going to the same partition.

Example

    java -jar connector-1.1.0.jar Publisher KAFKA 1 172.28.128.4 9092 test ASCII org.apache.kafka.common.serialization.StringSerializer org.apache.kafka.common.serialization.StringSerializer 2 1

Also you can use [kafkameter](https://github.com/BrightTag/kafkameter) that is JMeter plugin to publish message on topic. It works very well.


### IBM MQ

Execute connector-x.x.x.jar with the below arguments:

* **Connector type**: Publisher
* **Message broker**: MQ
* **Number of Publishers**: The number of publishers that are going to access Topic.
* **Hostname**: Host name or ip address where MQ Server is running.
* **Port**: Port number of the MQ Server listener.
* **Topic**: MQ topic name to publish message.
* **Encoding**: Character encoding standard for your message: For EBCDIC put Cp1047. ASCII just put ASCII.
* **MQ manager**: MQ Manager name. You can find it through IBM WebSphere MQ Explore or console.
* **MQ channel**: The Server channel name on MQ Server.
* **User id**: The userID to connect to MQ server channel. Leave it empty if you don't need user id to connect MQ.
* **Password**: The user password to connect to MQ server channel. Leave it empty if you don't need user id and password to connect MQ.

Example

    java -jar connector-1.1.0.jar Publisher MQ 3 127.0.0.1 1414 device ASCII IOTMQ CLIENT.TO.IOTMQ JoseSR 123456

Also there is JMeter plugin to connect MQ server through server channel and publish message on topic, it is [mqmeter](https://github.com/JoseLuisSR/mqmeter).
I made it and works good.


## Subscriber

### Kafka

Please execute the connector-x.x.x.jar file with the below arguments:

* **Connector type**: Subscriber
* **Message broker**: KAFKA
* **Number of Subscribers**: The number of subscribers that are going to subscribe on Topic.
* **Hostname**: Host name or ip address where Kafka Server is running. Vagrant file was configured with ip address 172.28.128.4, of course you can change it.
* **Port**: Port number of the Kafka Server. Vagrant file was configured to use 9092 port from host machine and it match with guest machine port where vagrant server is running.
* **Topic**: Kafka topic name to publish message. The install.sh file contains the commands to create topic 'test'.
* **Encoding**: Character encoding standard for your message: For EBCDIC put Cp1047. ASCII just put ASCII.
* **Group id**: Kafka consumers (subscribers) are part of a consumer group.
* **Message Deserializer**: Kafka class to deserializer the message. It is related with the kafka class used to serialize the message before sending through network.
* **Key Deserializer**: Kafka class to deserializer the key. It is related with the kafka class used to serialize the message before sending through network.

Example

    java -jar connector-1.1.0.jar Subscriber KAFKA 2 172.28.128.4 9092 test ASCII device org.apache.kafka.common.serialization.StringDeserializer org.apache.kafka.common.serialization.StringDeserializer


### IBM MQ

Similar as publisher you need execute connect-x.x.x.jar file with the below arguments:

* **Connector type**: Subscriber
* **Message broker**: MQ
* **Number of Subscribers**: The number of subscribers that are going to subscribe on Topic.
* **Hostname**: Host name or ip address where MQ Server is running.
* **Port**: Port number of the MQ Server listener.
* **Topic**: MQ topic name to receive message.
* **Encoding**: Character encoding standard for your message: For EBCDIC put Cp1047. ASCII just put ASCII.
* **MQ manager**: MQ Manager name. You can find it through IBM WebSphere MQ Explore or console.
* **MQ channel**: The Server channel name on MQ Server.
* **User id**: The userID to connect to MQ server channel. Leave it empty if you don't need user id to connect MQ.
* **Password**: The user password to connect to MQ server channel. Leave it empty if you don't need user id and password to connect MQ.

Example

    java -jar connector-1.1.0.jar Subscriber MQ 3 127.0.0.1 1414 device ASCII IOTMQ CLIENT.TO.IOTMQ JoseSR 123456
