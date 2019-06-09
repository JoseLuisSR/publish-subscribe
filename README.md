# Subscriber

Library to subscribe topic on MQ and Apache Kafka.

## Build

Build uber-jar through maven:

    mvn package

## Test

Execute Uber-jar with below parameters:

* **Subscriber**: MQ.
* **Hostname**: Host name or ip address where MQ Server is running.
* **Port**: Port number of the MQ Server listener.
* **MQ manager**: MQ Manager name. You can find it through IBM WebSphere MQ Explore or console.
* **MQ channel**: The Server channel name on MQ Server.
* **Topic**: MQ topic name to publish message.
* **Number or subscriber**: The number of topic subscribers that you want.
* **User id**: The userID to connect to MQ server channel. Put 'null' if you don't need user id to connect to MQ.
* **Password**: The user password to connect to MQ server channel. Put 'null' if you don't need user id and password to connect to MQ.