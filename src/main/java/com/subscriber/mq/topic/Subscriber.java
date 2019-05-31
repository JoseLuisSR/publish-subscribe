package com.subscriber.mq.topic;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import java.io.UnsupportedEncodingException;

public class Subscriber extends Thread{

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

    private int threadNo;

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
    public Subscriber(String hostname, Integer port, String channelServer, String userID,
                      String password, String mqManager, String topicName) throws MQException {
        MQEnvironment.hostname = hostname;
        MQEnvironment.port = port;
        MQEnvironment.channel = channelServer;
        if( userID != null )
            MQEnvironment.userID = userID;
        if( password != null )
            MQEnvironment.password = password;

        try{
            mqQueueManager = new MQQueueManager(mqManager);
            subscriber = mqQueueManager.accessTopic(null, topicName, CMQC.MQTOPIC_OPEN_AS_SUBSCRIPTION, openOptionsForGet);
        }catch (MQException e){
            if (mqQueueManager != null)
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
    public String receiveMessage() throws MQException, UnsupportedEncodingException {
        MQMsg2 mqMsg2 = new MQMsg2();
        MQGetMessageOptions mgmo = new MQGetMessageOptions();
        mgmo.options = CMQC.MQGMO_WAIT;
        mgmo.waitInterval = CMQC.MQWI_UNLIMITED;
        subscriber.getMsg2(mqMsg2,mgmo);
        if(encodingMsg == null )
            encodingMsg = "ASCII";
        return new String(mqMsg2.getMessageData(),encodingMsg);
    }

    /**
     * Unsubscribe MQ Topic and disconnect to MQ Manager.
     * @throws MQException
     */
    public void close() throws MQException{
        try {
            if (subscriber != null)
                subscriber.close();
            if (mqQueueManager != null)
                mqQueueManager.disconnect();
        }catch (MQException e){
            if (mqQueueManager != null)
                mqQueueManager.disconnect();
            throw e;
        }
    }

    /**
     * Execute subscriber thread.
     */
    @Override
    public void run() {
        try{
            while( true ){
                System.out.println(receiveMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                close();
            }catch (Exception e){
                e.printStackTrace();
            }
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
