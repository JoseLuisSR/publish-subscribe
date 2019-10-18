import com.subscriber.Subscriber;

public class SubscriberThread extends Thread{

    private Subscriber subscriber;

    public SubscriberThread(Subscriber subscriber, String name){
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
                subscriber.unsubscribe();
                subscriber.disconnect();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}