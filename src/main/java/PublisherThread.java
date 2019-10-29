import com.connector.Connector;

import java.util.Properties;

public class PublisherThread extends Thread{

    private Connector publisher;

    private Properties options;

    private volatile boolean exit = false;

    public PublisherThread(Connector publisher, String name, Properties options){
        super(name);
        this.publisher = publisher;
        this.options = options;
    }

    @Override
    public void run() {
        String mssgEvnt = "";
        while(!exit) {
            System.out.println("Enter message, event to publish on topic: ");
            mssgEvnt = System.console().readLine();
            publisher.announce(mssgEvnt, options);
        }
        publisher.leaveTopic();
        publisher.disconnect();
    }

    public void stoper(){
        this.exit = true;
    }

}
