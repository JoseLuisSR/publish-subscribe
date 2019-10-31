/*
 * Copyright 2019
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 */
package com.connector;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * This class is to subscribe on topic of Kafka server.
 * @author JoseLuisSR
 * @since 05/30/2019
 * @see "https://github.com/JoseLuisSR/subscriber"
 */
public class Kafka implements Connector {

    /**
     * Subscribers group identification constant.
     */
    public static final String GROUP_ID = "Subscriber group id to join";

    /**
     * Key deserializer class constant.
     */
    public static final String KEY_DESERIALIZER_CLASS = "Deserializer key object";

    /**
     * Value deserializer class constant.
     */
    public static final String VALUE_DESERIALIZER_CLASS = "Serializer value object";

    /**
     * Key serializer class constant.
     */
    public static final String KEY_SERIALIZER_CLASS = "Serializer key object";

    /**
     * Value serializer class constant.
     */
    public static final String VALUE_SERIALIZER_CLASS = "Serializer value object";

    /**
     * Topic partition constant.
     */
    public static final String TOPIC_PARTITION ="Partition number of topic";

    /**
     *Topic key constant.
     */
    public static final String TOPIC_KEY = "Event identify to consistency processing";

    /**
     * Auto offset constant.
     */
    public static final String AUTO_OFFSET = "earliest";

    /**
     *  Max poll record constant.
     */
    public static final String MAX_POLL_RECORDS = "1";

    /**
     * Kafka server properties.
     */
    private Properties serverProperties;

    /**
     * Consumer connector.
     */
    private KafkaConsumer<Object, Object> consumer;

    /**
     * Producer connector.
     */
    private KafkaProducer<Object, Object> producer;

    /**
     * Connect to kafka message broker.
     * @param properties
     */
    public void connect(Properties properties) {

        serverProperties = new Properties();
        serverProperties.put(HOST_NAME, properties.getProperty(HOST_NAME));
        serverProperties.put(PORT, properties.getProperty(PORT));
    }

    /**
     * Disconnect kafka message broker.
     */
    public void disconnect(){

    }

    /**
     * Subscribe topic on Kafka.
     * @param properties to connect message broker and topic.
     */
    public void subscribeTopic(Properties properties) {

        Properties consumerProperties = new Properties();

        // Kafka broker address.
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverProperties.getProperty(HOST_NAME)+":"+Integer.parseInt(serverProperties.getProperty(PORT)));
        // Subscribe group id.
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getProperty(GROUP_ID));
        // The class name to deserialize the key object.
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, properties.getProperty(KEY_DESERIALIZER_CLASS));
        // The class name to deserialize the value object.
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, properties.getProperty(VALUE_DESERIALIZER_CLASS));
        // Max records to get from topic
        consumerProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);
        //Starting offset should be.
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET);

        consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(Arrays.asList(properties.getProperty(TOPIC_NAME)));
    }

    /**
     * Unsubscribe & close topic on Kafka.
     */
    public void unsubscribeTopic() {
        consumer.unsubscribe();
        consumer.close();
    }

    /**
     * Listen Kafka topic to receive message.
     * @return message.
     */
    public String listen(Properties options){
        // Set unlimited time to wait get the message from topic.
        String message = (String) consumer.poll(Duration.ofMillis(Long.MAX_VALUE)).iterator().next().value();
        consumer.commitAsync();
        return message;
    }

    /**
     *Access topic on Kafka.
     * @param properties
     */
    public void accessTopic(Properties properties) {

        Properties producerProperties = new Properties();
        // Kafka broker address.
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getProperty(HOST_NAME)+":"+Integer.parseInt(properties.getProperty(PORT)));
        // The class name to serializer the key object.
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, properties.getProperty(KEY_SERIALIZER_CLASS));
        // The class name to serializer the value object.
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, properties.getProperty(VALUE_SERIALIZER_CLASS));
        producerProperties.put(ProducerConfig.ACKS_CONFIG, "1");

        producer = new KafkaProducer<>(producerProperties);
    }

    /**
     * Leave topic on Kafka.
     */
    public void leaveTopic() {
        producer.close();
    }

    /**
     * Announce event, message to the Topic on Kafka.
     * @param event or message to publish on topic.
     * @param options to set up the publish.
     */
    public void announce(String event, Properties options) {
        ProducerRecord<Object, Object> producerRecord = new ProducerRecord<>(options.getProperty(TOPIC_NAME),
                Integer.parseInt(options.getProperty(TOPIC_PARTITION)),options.getProperty(TOPIC_KEY), event);
        producer.send(producerRecord);

    }

}