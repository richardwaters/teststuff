import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class TestThreading {

    public static void main(String[] args) throws InterruptedException {

        final LinkedTransferQueue<String> linkedTransferQueue = new LinkedTransferQueue<>();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        long lastNewData = 0;

        while(true) {
            List<String> data = getData();

            if(data.size() > 0) {
                lastNewData = System.currentTimeMillis();
            }

            for(String s : data) {

            Future f = executorService.submit(() -> {
                try {
                    System.out.println("Taking");
                   ;

                    System.out.println("Taken " +  linkedTransferQueue.take());
                    Thread.sleep(5000);
                    //linkedTransferQueue.notify();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
                System.out.println("Adding "+s);
                linkedTransferQueue.transfer(s);
                System.out.println("Added "+s);
            }

            if(data.size() == 0 && !linkedTransferQueue.hasWaitingConsumer()) {
                System.out.println("Empty");
                if(System.currentTimeMillis() - lastNewData > 10000) {
                    System.out.println("No new data in 10 sec");
                    break;
                } else {
                    System.out.println("Waiting 10 sec");
                    Thread.sleep(10000);
                }
            }

        }
        executorService.shutdown();
    }

    static int x = 5;

    static List<String> getData() {
        String[] s = new String[x];
        for(int n=0 ; n<x ; n++) {
            s[n] = x + "-" + n;
        }
        if(x>0) {
            x--;
        }
        return Arrays.asList(s);
    }

}
