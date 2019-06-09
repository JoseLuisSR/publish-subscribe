
import com.ibm.mq.MQException;
import com.subscriber.mq.topic.Subscriber;

public class SubscriberTest {

    private String hostname;
    
    private Integer port;
    
    private String mqManager;

    private String channel;

    private String topicName;

    private Integer subsNumber;

    private String userID;

    private String password;

    public SubscriberTest(String args[]){

        if( args[0].equals("MQ") ) {
            //127.0.0.1;
            hostname = args[1];
            //1414
            port = Integer.parseInt(args[2]);
            //IOTMQ
            mqManager = args[3];
            //CLIENT.TO.IOTMQ
            channel = args[4];
            //device
            topicName = args[5];
            //How many subscribers.
            subsNumber = Integer.parseInt(args[6]);
            //JoseSR
            userID = args[7];
            //Password
            password = args[8];
        }else if (args[0].equals("Kafka")){

        }
    }

    public static void main (String args[]){

        SubscriberTest test = new SubscriberTest(args);
        test.launchSubscriber();
    }

    private void launchSubscriber(){
        Thread[] subscribers = new Thread[subsNumber];
        Subscriber subscriber;

        for(int i = 0; i < subsNumber; i++) {
            try {
                subscriber = new Subscriber(hostname, port, mqManager, channel, topicName, userID, password);
                subscribers[i] = new MQSubscriber(subscriber, "Subscriber " + i);
                subscribers[i].start();
            }catch (MQException e){
                e.printStackTrace();
            }
        }
    }

     class MQSubscriber extends Thread{

        private Subscriber subscriber;

        public MQSubscriber(Subscriber subscriber, String name){
            super(name);
            this.subscriber = subscriber;
        }

        @Override
        public void run(){
            try{
                while( true ){
                    System.out.println(this.getName() + " -> Message: " + subscriber.receiveMessage());
                }
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                try{
                    subscriber.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
