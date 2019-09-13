package com.subscriber.mq.topic;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMsg2;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.MQTopic;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.MQConstants;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

public class Subscriber{

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
     * Constructor to connect to MQ Manager and MQ topic like subscriber.
     * @param hostname
     * @param port
     * @param channelServer
     * @param userID
     * @param password
     * @param mqManager
     * @param topicName
     * @throws MQException
     */
    public Subscriber(String hostname, Integer port, String mqManager, String channelServer,
                      String topicName, String userID, String password) throws MQException {

        // SET MQ Manager properties to connection.
        Hashtable properties = new Hashtable<String, Object>();
        properties.put(MQConstants.HOST_NAME_PROPERTY, hostname);
        properties.put(MQConstants.PORT_PROPERTY, port);
        properties.put(MQConstants.CHANNEL_PROPERTY, channelServer);
        properties.put(MQConstants.USE_MQCSP_AUTHENTICATION_PROPERTY, true);
        if( userID != null && !userID.isEmpty() )
            properties.put(MQConstants.USER_ID_PROPERTY, userID);
        if( password != null && !password.isEmpty() )
            properties.put(MQConstants.PASSWORD_PROPERTY, password);

        try{
            mqQueueManager = new MQQueueManager(mqManager, properties);
            subscriber = mqQueueManager.accessTopic(null, topicName, CMQC.MQTOPIC_OPEN_AS_SUBSCRIPTION, openOptionsForGet);
        }catch (MQException e){

            if ( mqQueueManager != null && mqQueueManager.isConnected() )
                mqQueueManager.disconnect();

            throw e;
        }
    }

    /**
     * Listen MQ Topic to receive message.
     * @return
     * @throws MQException
     * @throws UnsupportedEncodingException
     */
    public String receiveMessage()throws MQException, UnsupportedEncodingException {
        String message;
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

            // Close subscriber.
            if (subscriber != null && subscriber.isOpen())
                subscriber.close();

            // Disconnect mq manager.
            if (mqQueueManager != null && mqQueueManager.isConnected())
                mqQueueManager.disconnect();

            throw e;

        }

        return message;
    }

    /**
     * Unsubscribe MQ Topic and disconnect to MQ Manager.
     * @throws MQException
     */
    public void close() throws MQException{
        try {
            // Close subscriber.
            if (subscriber != null && subscriber.isOpen())
                subscriber.close();

            // Disconnect mq manager.
            if (mqQueueManager != null && mqQueueManager.isConnected())
                mqQueueManager.disconnect();

        }catch (MQException e){

            if (mqQueueManager != null && mqQueueManager.isConnected())
                mqQueueManager.disconnect();

            throw e;
        }
    }

    /**
     * Set encoding message attribute.
     * @param encodingMsg
     */
    public void setEncodingMsg(String encodingMsg) {
        this.encodingMsg = encodingMsg;
    }

    /**
     * Get encoding message attribute.
     * @return encodingMsg.
     */
    public String getEncodingMsg() {
        return encodingMsg;
    }
}
