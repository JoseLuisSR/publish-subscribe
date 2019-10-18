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
package com.subscriber;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * This class is to subscribe on topic of Kafka server.
 * @author JoseLuisSR
 * @since 05/30/2019
 * @see "https://github.com/JoseLuisSR/subscriber"
 */
public class KafkaSubscriber implements Subscriber{

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
    public static final String VALUE_DESERIALIZER_CLASS = "Deserializer value object";

    /**
     * Auto offset constant.
     */
    public static final String AUTO_OFFSET = "earliest";

    /**
     *  Max poll record constant.
     */
    public static final String MAX_POLL_RECORDS = "1";

    /**
     * Kafka topic subscriber.
     */
    private Consumer<Long, String> subscriber;

    /**
     * Kafka Message Broker constructor.
     */
    public KafkaSubscriber(){

    }

    /**
     * Connect to kafka message broker.
     * @param properties
     */
    public void connect(Properties properties) {
        Properties kafkaProperties = new Properties();
        // Kafka broker address.
        kafkaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getProperty(HOST_NAME)+":"+Integer.parseInt(properties.getProperty(PORT)));
        // Subscribe group id.
        kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getProperty(GROUP_ID));
        // The class name to deserialize the key object.
        kafkaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, properties.getProperty(KEY_DESERIALIZER_CLASS));
        // The class name to deserialize the value object.
        kafkaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, properties.getProperty(VALUE_DESERIALIZER_CLASS));
        // Max records to get from topic
        kafkaProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);
        //Starting offset should be.
        kafkaProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET);

        subscriber = new KafkaConsumer<>(kafkaProperties);
    }

    /**
     * Subscribe topic on Kafka.
     * @param properties to connect message broker and topic.
     */
    public void subscribe(Properties properties) {

        subscriber.subscribe(Arrays.asList(properties.getProperty(TOPIC_NAME)));
    }

    /**
     * Unsubscribe topic on Kafka.
     */
    public void unsubscribe() {
        subscriber.unsubscribe();
    }

    /**
     * Listen Kafka topic to receive message.
     * @return message.
     */
    public String receiveMessage(){
        // Set unlimited time to wait get the message from topic.
        String message = subscriber.poll(Duration.ofMillis(Long.MAX_VALUE)).iterator().next().value();
        subscriber.commitAsync();
        return message;
    }

    /**
     * Disconnect kafka message broker.
     */
    public void disconnect(){
        subscriber.close();
    }
    
}