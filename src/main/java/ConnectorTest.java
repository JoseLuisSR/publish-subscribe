public class ConnectorTest {

    public static void main(String [] args){

        if(args[0].equals("Publisher")){
            PublisherTest publisher = new PublisherTest(args);
            publisher.launch();
        }else if(args[0].equals("Subscriber")){
            SubscriberTest subscriber = new SubscriberTest(args);
            subscriber.launch();
        }
    }
}
