package com.subscriber.kafka.topic;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class Subscriber {

    private KafkaConsumer<String, String> subscribers;

    public Subscriber(String hostname, Integer port, String mqManager, String channelServer,
                      String topicName, String userID, String password){

        Properties props = new Properties();
        props.put("bootstrap.servers", hostname+":"+port);
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topicName));
    }
}
