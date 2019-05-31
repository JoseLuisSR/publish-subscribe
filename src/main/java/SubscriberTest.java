import com.subscriber.mq.topic.Subscriber;

public class SubscriberTest {

    public static void main (String args[]){

        String hostname = "127.0.0.1";
        Integer port = 1414;
        String channel = "CLIENT.TO.IOTMQ";
        String userID = "JoseSR";
        String password = null;
        String mqManager = "IOTMQ";
        String topicName = "device";
        Integer threadNumber = 4;
        Thread[] subscribers = new Thread[threadNumber];

        for(int i = 0; i < threadNumber; i++) {
            try {
                subscribers[i] = new Subscriber(hostname, port, channel, userID, password, mqManager, topicName);
                subscribers[i].start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
