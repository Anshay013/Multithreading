import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class Main {

  public static final String TAG = "MultiThreading = ";

    static int cnt = 0;
    public static synchronized void Inc() {
          ++ cnt;
    }

    public static void main(String[] args) {

        ThreadPoolExecutor exec = new ThreadPoolExecutor(
                2,                             // corePoolSize
                8,                             // maximumPoolSize
                0L, TimeUnit.MILLISECONDS,     // keepAliveTime, core thread never timed out -> unless call : allowCoreThreadTimeOut(true)
                new LinkedBlockingQueue<Runnable>(), // unbounded queue (default capacity = Integer.MAX_VALUE)
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy() // rejected execution handler
        );

        List<Future<?>>futures = new ArrayList<>();

        for(int i = 0 ;i < 5; ++ i){
            Callable task = () ->{
                 Inc();
                 return cnt;
            };

            futures.add(exec.submit(task));
        }

        try {


            exec.shutdown();

            List<Runnable> pending = new ArrayList<>();
           // pending = exec.shutdownNow(); //--> here it can give some value since its sudden and there might be task which hasn't started yet.

            // // it returns true if under 5 seconds executor terminates, or true directly if  executor before reaching this was terminated, else false
            if(!exec.awaitTermination(5, TimeUnit.SECONDS)) {
                // so if executor doesnt terminates under 5 seconds, terminate it, destroyed all ongoing and queued task
                pending = exec.shutdownNow();
            }

            // check if all task are completed future status.
            boolean completed = futures
                    .stream()
                    .allMatch(x -> x.isDone());

            if(completed) System.out.println(TAG + " " + "all task completed");
             System.out.println(TAG + " " +  String.valueOf(pending.size()));

        }
        catch (Exception e){
            Logger.getLogger("Exception", e.getMessage());
        }





    }
}