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

import java.util.Properties;

/**
 * This class is an interface with the common behaviour between Kafka and MQ.
 * @author JoseLuisSR
 * @since 05/30/2019
 * @see "https://github.com/JoseLuisSR/subscriber"
 */
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
