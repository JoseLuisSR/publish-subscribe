import com.connector.Connector;

import java.util.Properties;

public class SubscriberThread extends Thread{

    private Connector subscriber;

    private Properties options;

    private volatile boolean exit = false;

    public SubscriberThread(Connector subscriber, String name, Properties options){
        super(name);
        this.subscriber = subscriber;
        this.options = options;
    }

    @Override
    public void run(){

        while(!exit){
            System.out.println(this.getName() + " -> Message: " + subscriber.listen(options));
        }
        subscriber.unsubscribeTopic();
        subscriber.disconnect();

        /*
        try{
            while( true ){
                System.out.println(this.getName() + " -> Message: " + subscriber.listen(options));
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                subscriber.unsubscribeTopic();
                subscriber.disconnect();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        */
    }

    public void stoper(){
        this.exit = true;
    }

}