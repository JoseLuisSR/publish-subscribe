package com.subscriber;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

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
     * Kafka topic subscriber.
     */
    private Consumer<Long, String> subscriber;

    /**
     * Kafka Message Broker constructor.
     */
    public KafkaSubscriber(){

    }

    /**
     * Subscribe topic on Kafka
     * @param properties to connect message broker and topic.
     */
    public void subscribe(Properties properties) {
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
        kafkaProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);

        subscriber = new KafkaConsumer<>(kafkaProperties);
        subscriber.subscribe(Arrays.asList(properties.getProperty(TOPIC_NAME)));
    }

    /**
     * Listen Kafka topic to receive message.
     * @return message.
     */
    public String receiveMessage(){
        // Set unlimited time to wait get the message from topic.
        String message = subscriber.poll(Duration.ofMillis(Long.MAX_VALUE)).records("test1").iterator().next().value();
        subscriber.commitAsync();
        return message;
    }

    /**
     * Unsubscribe kafka topic.
     */
    public void close(){
        subscriber.close();
    }
    
}