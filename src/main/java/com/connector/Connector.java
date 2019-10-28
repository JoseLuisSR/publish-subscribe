package com.connector;

import com.connector.publisher.Publisher;
import com.connector.subscriber.Subscriber;

import java.util.Properties;

public interface Connector extends Publisher, Subscriber {

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
     * Encoding constant.
     */
    String ENCODING_MSG_EVENT = "Encoding message or event.";

    /**
     * Connect to message broker.
     * @param properties
     */
    void connect(Properties properties);

    /**
     * Disconnect message broker.
     */
    void disconnect();
}
