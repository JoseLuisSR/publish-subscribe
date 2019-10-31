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

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.MQConstants;

import java.io.IOException;
import java.util.Properties;

/**
 * This class is connector to IBM MQ Message Broker.
 * @author JoseLuisSR
 * @since 05/30/2019
 * @see "https://github.com/JoseLuisSR/subscriber"
 */
public class MQ implements Connector {

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
     * Constant to subscribe to MQ topic.
     */
    private final int openOptionsForGet = CMQC.MQSO_CREATE | CMQC.MQSO_FAIL_IF_QUIESCING
            | CMQC.MQSO_MANAGED | CMQC.MQSO_NON_DURABLE;

    /**
     * MQ Manager attribute.
     */
    private MQQueueManager mqQueueManager;

    /**
     * Consumer connector.
     */
    private MQTopic consumer;

    /**
     * Producer connector.
     */
    private MQTopic producer;

    /**
     * Connect to MQ message broker.
     * @param properties
     */
    public void connect(Properties properties) {
        // SET MQ Manager properties to connection.
        Properties mqProperties = new Properties();
        //MQ host
        mqProperties.put(MQConstants.HOST_NAME_PROPERTY, properties.getProperty(HOST_NAME));
        //MQ port
        mqProperties.put(MQConstants.PORT_PROPERTY, Integer.parseInt(properties.getProperty(PORT)));
        //MQ Server channel.
        mqProperties.put(MQConstants.CHANNEL_PROPERTY, properties.getProperty(CHANNEL_SRV_NAME));
        //MQ Authentication property.
        mqProperties.put(MQConstants.USE_MQCSP_AUTHENTICATION_PROPERTY, true);
        String userID = properties.getProperty(USER_ID);

        if( userID != null && !userID.isEmpty() ){
            //MQ User name.
            mqProperties.put(MQConstants.USER_ID_PROPERTY, userID);
        }

        String password = properties.getProperty(PASSWORD);
        if( password != null && !password.isEmpty() ){
            //MQ User password.
            mqProperties.put(MQConstants.PASSWORD_PROPERTY, password);
        }

        try{
            mqQueueManager = new MQQueueManager(properties.getProperty(MQ_MANAGER), mqProperties);

        }catch (MQException e){
            e.printStackTrace();
        }
    }

    /**
     * Disconnect MQ message broker.
     */
    public void disconnect(){
        try {
            mqQueueManager.disconnect();
        }catch (MQException e){
            e.printStackTrace();
        }
    }

    /**
     * Subscribe topic on MQ.
     * @param properties to connect message broker and topic.
     */
    public void subscribeTopic(Properties properties) {

        try{
            consumer = mqQueueManager.accessTopic(null, properties.getProperty(TOPIC_NAME), CMQC.MQTOPIC_OPEN_AS_SUBSCRIPTION, openOptionsForGet);
        }catch (MQException e){
            e.printStackTrace();
        }
    }

    /**
     * Unsubscribe topic on MQ.
     */
    public void unsubscribeTopic() {
        try{
            consumer.close();
        }catch (MQException e){
            e.printStackTrace();
        }
    }

    /**
     * Listen mq topic to receive message
     * @return message
     */
    public String listen(Properties options){
        String message = "";
        MQMsg2 mqMsg2 = new MQMsg2();
        MQGetMessageOptions mqMsgOpt = new MQGetMessageOptions();
        mqMsgOpt.options = CMQC.MQGMO_WAIT;
        mqMsgOpt.waitInterval = CMQC.MQWI_UNLIMITED;

        try {
            consumer.getMsg2(mqMsg2, mqMsgOpt);
            message = new String(mqMsg2.getMessageData(), options.getProperty(ENCODING_MSG_EVENT));
        }catch (MQException e){
            unsubscribeTopic();
            disconnect();
            e.printStackTrace();

        }finally {
            return message;
        }
    }

    /**
     * Access topic on IBM MQ.
     * @param properties
     */
    public void accessTopic(Properties properties) {

        try{
            producer = mqQueueManager.accessTopic(null, properties.getProperty(TOPIC_NAME), MQConstants.MQTOPIC_OPEN_AS_PUBLICATION, MQConstants.MQOO_OUTPUT);
        }catch (MQException e){
            e.printStackTrace();
        }
    }

    /**
     * Leave topic on IBM MQ.
     */
    public void leaveTopic() {
        try{
            producer.close();
        }catch (MQException e){
            e.printStackTrace();
        }
    }

    /**
     * Announce event, message to the Topic on IBM MQ.
     * @param event or message to publish on topic.
     * @param options to set up the publish.
     */
    public void announce(String event, Properties options) {

        MQMessage mqMessage = new MQMessage();
        try{
            mqMessage.write(event.getBytes(options.getProperty(ENCODING_MSG_EVENT)));
            producer.put(mqMessage, new MQPutMessageOptions());
        }catch (MQException | IOException e){
            leaveTopic();
            disconnect();
            e.printStackTrace();
        }
    }

}
