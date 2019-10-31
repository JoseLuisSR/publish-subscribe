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
package com.connector.subscriber;

import java.util.Properties;

/**
 * This class is an interface for subscriber connector.
 * @author JoseLuisSR
 * @since 05/30/2019
 * @see "https://github.com/JoseLuisSR/subscriber"
 */
public interface Subscriber {

    /**
     * Subscribe topic.
     * @param properties to subscribe topic.
     */
    void subscribeTopic(Properties properties);

    /**
     * Unsubscribe topic.
     */
    void unsubscribeTopic();

    /**
     * Listen topic to receive event, message.
     * @param options to set up consumer.
     * @return event or message from topic.
     */
    String listen(Properties options);

}
