import com.connector.Connector;
import com.connector.Kafka;
import com.connector.MQ;

import java.util.Properties;

public class PublisherTest {

    private Properties properties;

    private int pubshNumber;

    private String messageBroker;

    public PublisherTest(String [] args){
        properties = new Properties();

        messageBroker = args[1];
        pubshNumber = Integer.parseInt(args[2]);
        properties.put(Connector.HOST_NAME, args[3]);
        properties.put(Connector.PORT,args[4]);
        properties.put(Connector.TOPIC_NAME, args[5]);
        properties.put(Connector.ENCODING_MSG_EVENT, args[6]);

        if( messageBroker.equals("MQ") ) {

            properties.put(MQ.MQ_MANAGER, args[7]);
            properties.put(MQ.CHANNEL_SRV_NAME, args[8]);
            properties.put(MQ.USER_ID, args[9]);
            properties.put(MQ.PASSWORD, args[10]);

            System.out.println( properties.getProperty(Connector.HOST_NAME) + " " +
                    properties.getProperty(Connector.PORT) + " " +  properties.getProperty(MQ.TOPIC_NAME) + " " +
                    properties.getProperty(MQ.MQ_MANAGER) + " " + properties.getProperty(MQ.CHANNEL_SRV_NAME) + " " +
                    properties.getProperty(MQ.USER_ID) + " " + properties.getProperty(MQ.PASSWORD) );


        }else if (messageBroker.equals("KAFKA")){

            properties.put(Kafka.KEY_SERIALIZER_CLASS, args[7]);
            properties.put(Kafka.VALUE_SERIALIZER_CLASS, args[8]);
            properties.put(Kafka.TOPIC_PARTITION, args[9]);
            properties.put(Kafka.TOPIC_KEY, args[10]);

            System.out.println( properties.getProperty(Connector.HOST_NAME) + " " +
                    properties.getProperty(Connector.PORT) + " " +  properties.getProperty(MQ.TOPIC_NAME) + " " +
                    properties.getProperty(Kafka.KEY_SERIALIZER_CLASS) + " " + properties.getProperty(Kafka.VALUE_SERIALIZER_CLASS) + " " +
                    properties.getProperty(Kafka.TOPIC_PARTITION) + " " + properties.getProperty(Kafka.TOPIC_KEY) );
        }

    }

    public void launch(){
        Thread[] publishers = new Thread[pubshNumber];
        Connector publisher;

        for(int i = 0; i < pubshNumber; i++) {
            publisher = messageBroker.equals("MQ") ? new MQ() : new Kafka();
            publisher.connect(properties);
            publisher.accessTopic(properties);
            publishers[i] = new PublisherThread(publisher, "Publisher " + i, properties);
            publishers[i].start();
            System.out.println("Publisher " + i + " Ready");
        }
    }
}
