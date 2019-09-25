package com.subscriber.kafka.topic;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class Subscriber {

    private Consumer<Long, String> subscriber;

    public Subscriber(String hostname, Integer port, String keyDeserializer, String valueDeserializer,
                      String topicName, String groupId){

        Properties props = new Properties();
        // Kafka broker address.
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, hostname+":"+port);
        // Subscribe group id.
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        // The class name to deserialize the key object.
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        // The class name to deserialize the value object.
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        // Max records to get from topic
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);

        subscriber = new KafkaConsumer<>(props);
        subscriber.subscribe(Arrays.asList(topicName));
    }

    /**
     * Listen topic to get message.
     * @return message.
     */
    public String receiveMessage(){
        // Set unlimited time to wait get the message from topic.
        String message = subscriber.poll(Duration.ofMillis(Long.MAX_VALUE)).records("test1").iterator().next().value();
        subscriber.commitAsync();
        return message;
    }

    /**
     * Close subscriber on topic.
     */
    public void close(){
        subscriber.close();
    }
    
}
