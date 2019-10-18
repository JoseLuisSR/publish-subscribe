
import com.subscriber.MQSubscriber;
import com.subscriber.KafkaSubscriber;
import com.subscriber.Subscriber;

import java.util.Properties;

public class SubscriberTest {

    private Properties subsProperties;

    private int subsNumber;

    public SubscriberTest(String args[]){

        subsProperties = new Properties();

        //How many subscribers.
        subsNumber = Integer.parseInt(args[1]);
        subsProperties.put(Subscriber.HOST_NAME, args[2]);
        subsProperties.put(Subscriber.PORT,args[3]);
        subsProperties.put(Subscriber.TOPIC_NAME, args[4]);

        if( args[0].equals("MQ") ) {

            subsProperties.put(MQSubscriber.MQ_MANAGER, args[5]);
            subsProperties.put(MQSubscriber.CHANNEL_SRV_NAME, args[6]);
            subsProperties.put(MQSubscriber.USER_ID, args[7]);
            subsProperties.put(MQSubscriber.PASSWORD, args[8]);

            System.out.println( subsProperties.getProperty(Subscriber.HOST_NAME) + " " +
                    subsProperties.getProperty(Subscriber.PORT) + " " +  subsProperties.getProperty(MQSubscriber.TOPIC_NAME) + " " +
                    subsProperties.getProperty(MQSubscriber.MQ_MANAGER) + " " + subsProperties.getProperty(MQSubscriber.CHANNEL_SRV_NAME) + " " +
                    subsProperties.getProperty(MQSubscriber.USER_ID) + " " + subsProperties.getProperty(MQSubscriber.PASSWORD) );

        }else if (args[0].equals("KAFKA")){

            subsProperties.put(KafkaSubscriber.GROUP_ID, args[5]);
            subsProperties.put(KafkaSubscriber.KEY_DESERIALIZER_CLASS, args[6]);
            subsProperties.put(KafkaSubscriber.VALUE_DESERIALIZER_CLASS, args[7]);

            System.out.println( subsProperties.getProperty(Subscriber.HOST_NAME) + " " +
                    subsProperties.getProperty(Subscriber.PORT) + " " +  subsProperties.getProperty(MQSubscriber.TOPIC_NAME) + " " +
                    subsProperties.getProperty(KafkaSubscriber.GROUP_ID) + " " + subsProperties.getProperty(KafkaSubscriber.KEY_DESERIALIZER_CLASS) + " " +
                    subsProperties.getProperty(KafkaSubscriber.VALUE_DESERIALIZER_CLASS) );

        }

    }

    public static void main (String args[]){

        SubscriberTest test = new SubscriberTest(args);
        test.launchSubscriber(args);
    }

    private void launchSubscriber(String args[]){
        Thread[] subscribers = new Thread[subsNumber];
        Subscriber subscriber;

        for(int i = 0; i < subsNumber; i++) {
            subscriber = args[0].equals("MQ") ? new MQSubscriber() : new KafkaSubscriber();
            subscriber.connect(subsProperties);
            subscriber.subscribe(subsProperties);
            subscribers[i] = new SubscriberThread(subscriber, "Subscriber " + i);
            subscribers[i].start();
            System.out.println("Subscriber " + i + " Ready");
        }
    }

}
