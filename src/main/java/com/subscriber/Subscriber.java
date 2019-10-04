package com.subscriber;

import java.util.Properties;

public interface Subscriber {

    /**
     * Host name constant.
     */
    String HOST_NAME = "Host name";

    /**
     * Port number constant.
     */
    String PORT = "Port number";

    /**
     * Topic name constant.
     */
    String TOPIC_NAME = "Topic name";

    /**
     * Subscribe topic.
     * @param properties to connect message broker and topic.
     */
    void subscribe(Properties properties);

    /**
     * Listen topic to receive message.
     * @return message
     */
    String receiveMessage();

    /**
     * Unsubscribe topic.
     */
    void close();

}
