
import com.connector.Connector;
import com.connector.Kafka;
import com.connector.MQ;

import java.util.Properties;

public class SubscriberTest {

    private Properties subsProperties;

    private int subsNumber;

    private String messageBroker;

    public SubscriberTest(String args[]){

        subsProperties = new Properties();

        messageBroker = args[1];
        //How many subscribers.
        subsNumber = Integer.parseInt(args[2]);
        subsProperties.put(Connector.HOST_NAME, args[3]);
        subsProperties.put(Connector.PORT,args[4]);
        subsProperties.put(Connector.TOPIC_NAME, args[5]);
        subsProperties.put(Connector.ENCODING_MSG_EVENT, args[6]);

        if( messageBroker.equals("MQ") ) {

            subsProperties.put(MQ.MQ_MANAGER, args[7]);
            subsProperties.put(MQ.CHANNEL_SRV_NAME, args[8]);
            subsProperties.put(MQ.USER_ID, args[9]);
            subsProperties.put(MQ.PASSWORD, args[10]);

            System.out.println( subsProperties.getProperty(Connector.HOST_NAME) + " " +
                    subsProperties.getProperty(Connector.PORT) + " " +  subsProperties.getProperty(MQ.TOPIC_NAME) + " " +
                    subsProperties.getProperty(MQ.MQ_MANAGER) + " " + subsProperties.getProperty(MQ.CHANNEL_SRV_NAME) + " " +
                    subsProperties.getProperty(MQ.USER_ID) + " " + subsProperties.getProperty(MQ.PASSWORD) );

        }else if (messageBroker.equals("KAFKA")){

            subsProperties.put(Kafka.GROUP_ID, args[7]);
            subsProperties.put(Kafka.KEY_DESERIALIZER_CLASS, args[8]);
            subsProperties.put(Kafka.VALUE_DESERIALIZER_CLASS, args[9]);

            System.out.println( subsProperties.getProperty(Connector.HOST_NAME) + " " +
                    subsProperties.getProperty(Connector.PORT) + " " +  subsProperties.getProperty(MQ.TOPIC_NAME) + " " +
                    subsProperties.getProperty(Kafka.GROUP_ID) + " " + subsProperties.getProperty(Kafka.KEY_DESERIALIZER_CLASS) + " " +
                    subsProperties.getProperty(Kafka.VALUE_DESERIALIZER_CLASS) );

        }

    }


    public void launch(){
        Thread[] subscribers = new Thread[subsNumber];
        Connector subscriber;

        for(int i = 0; i < subsNumber; i++) {
            subscriber = messageBroker.equals("MQ") ? new MQ() : new Kafka();
            subscriber.connect(subsProperties);
            subscriber.subscribeTopic(subsProperties);
            subscribers[i] = new SubscriberThread(subscriber, "Subscriber " + i, subsProperties);
            subscribers[i].start();
            System.out.println("Subscriber " + i + " Ready");
        }
    }

}
