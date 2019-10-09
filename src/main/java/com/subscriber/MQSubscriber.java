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

import com.ibm.mq.MQException;
import com.ibm.mq.MQMsg2;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.MQTopic;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.MQGetMessageOptions;
import java.util.Properties;

/**
 * This class is to subscribe on topic of MQ server.
 * @author JoseLuisSR
 * @since 05/30/2019
 * @see "https://github.com/JoseLuisSR/subscriber"
 */
public class MQSubscriber implements Subscriber {

    /**
     * MQ Channel Server Name constant.
     */
    public static final String CHANNEL_SRV_NAME = "Channel server name";

    /**
     * MQ Manager name constant.
     */
    public static final String MQ_MANAGER = "MQ Manager";

    /**
     * User id constant.
     */
    public static final String USER_ID = "User id";

    /**
     * User password constant.
     */
    public static final String PASSWORD = "Password";

    /**
     * MQ Manager attribute.
     */
    private MQQueueManager mqQueueManager;

    /**
     * MQ Topic attribute.
     */
    private MQTopic subscriber;

    /**
     * Encoding message attribute.
     */
    private String encodingMsg;

    /**
     * Constant to subscribe to MQ topic.
     */
    private final int openOptionsForGet = CMQC.MQSO_CREATE | CMQC.MQSO_FAIL_IF_QUIESCING
            | CMQC.MQSO_MANAGED | CMQC.MQSO_NON_DURABLE;


    /**
     * MQ Message Broker Constructor.
     */
    public MQSubscriber(){
        
    }

    /**
     * Subscribe topic on MQ.
     * @param properties to connect message broker and topic.
     */
    public void subscribe(Properties properties) {
        // SET MQ Manager properties to connection.
        Properties mqProperties = new Properties();
        mqProperties.put(MQConstants.HOST_NAME_PROPERTY, properties.getProperty(HOST_NAME));
        mqProperties.put(MQConstants.PORT_PROPERTY, Integer.parseInt(properties.getProperty(PORT)));
        mqProperties.put(MQConstants.CHANNEL_PROPERTY, properties.getProperty(CHANNEL_SRV_NAME));
        mqProperties.put(MQConstants.USE_MQCSP_AUTHENTICATION_PROPERTY, true);
        String userID = properties.getProperty(USER_ID);
        if( userID != null && !userID.isEmpty() )
            mqProperties.put(MQConstants.USER_ID_PROPERTY, userID);
        String password = properties.getProperty(PASSWORD);
        if( password != null && !password.isEmpty() )
            mqProperties.put(MQConstants.PASSWORD_PROPERTY, password);

        try{
            mqQueueManager = new MQQueueManager(properties.getProperty(MQ_MANAGER), mqProperties);
            subscriber = mqQueueManager.accessTopic(null, properties.getProperty(TOPIC_NAME), CMQC.MQTOPIC_OPEN_AS_SUBSCRIPTION, openOptionsForGet);
        }catch (MQException e){
            try{
                subscriber.close();
                mqQueueManager.disconnect();
            }catch (MQException e1){

            }
        }
    }

    /**
     * Listen mq topic to receive message
     * @return message
     */
    public String receiveMessage(){
        String message = "";
        MQMsg2 mqMsg2 = new MQMsg2();
        MQGetMessageOptions mgmo = new MQGetMessageOptions();
        mgmo.options = CMQC.MQGMO_WAIT;
        mgmo.waitInterval = CMQC.MQWI_UNLIMITED;

        try {

            subscriber.getMsg2(mqMsg2, mgmo);
            if (encodingMsg == null)
                encodingMsg = "ASCII";
            message = new String(mqMsg2.getMessageData(), encodingMsg);

        }catch (MQException e){

            try{
                subscriber.close();
                mqQueueManager.close();

            }catch (MQException e1){

            }

        }finally {
            return message;
        }
    }

    /**
     * Unsubscribe mq topic.
     */
    public void close(){
        try {
            subscriber.close();
            mqQueueManager.disconnect();
        }catch (MQException e){
        }
    }

}
