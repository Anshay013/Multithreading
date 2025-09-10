import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/*

 This class helps us define a custom ExecutorFactory


* */

public class FixedPoolDemo {


    static class NameFactory implements ThreadFactory {


        private final AtomicInteger pool = new AtomicInteger(1); // this is help us to know the pool id
        private final AtomicInteger thread = new AtomicInteger(1); // current no of threads in the pool.


        private final String prefix = "demoPool-" + pool.getAndIncrement() + "-thread-"; // name thread created by this factory

        //  NamedFactory: a simple custom ThreadFactory implementation
        //  ThreadFactory controls how new Threads are created for the pool


        // Here you see we are the ones creating the thread, by ourself and giving it to the factory
        @Override
        public Thread newThread(Runnable r) { // called by ThreadPool executor whenever we need a new thread.
            // here we create a new thread

            Thread t = new Thread(r, prefix + thread.getAndIncrement()); // task, thread name

            t.setDaemon(false); // make it non daemon so that JVM waits until it finishes.

            t.setPriority(Thread.NORM_PRIORITY); // setting middle priority


            // this is something new, it catches any task running on this thread which may throw an exception
            t.setUncaughtExceptionHandler((th, ex) -> {
                System.err.println("Uncaught exception in " + th.getName() + ": " + ex);
            });

            return t;
        }

    }


    // New : How to run the task or runnable because in the above we just returned the thread.
    // when we say runnable.run() then its a thread which does this execution, so here which thread does it.
    // The above new Thread which is returned does this running.


    static Runnable withLoggingAndCleanup(Runnable r, String label) {
        return () -> {
            // capture the current thread name for clearer log messages
            String threadName = Thread.currentThread().getName();

            // log start of the logical task
            System.out.println("START " + label + " on " + threadName);


            try {
                // execute the original runnable's work
                r.run();
            } finally {
                // or to ensure the "END" message is printed even on exceptions
                System.out.println("END " + label + " on " + threadName);
            }
        };
    }

    public static void main(String[] args) {


        ThreadPoolExecutor fixed = new ThreadPoolExecutor(
                3,
                4,
                0L, // it means none of the threads will time out immediately
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new NameFactory(),
                new ThreadPoolExecutor.AbortPolicy() // rejects new tasks by throwing an exception (normally only after shutdown with unbounded queue (huge len))
        ) {
            // these three are overriden methods (extend, doesn't mean we need to override them) which lets us know or perform things when service starts or ends.
            //LIFE CYCLE methods of ThreadPoolExecutor

            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                System.out.println("beforeExecute: " + t.getName());
            }

            // Override afterExecute hook — called after task completes or throws
            // The Throwable t parameter is non-null only for certain execution paths
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                System.out.println("afterExecute: " + Thread.currentThread().getName() + " throwable=" + t);
            }


            // Override terminated hook — called once when the pool transitions to TERMINATED
            @Override
            protected void terminated() {
                System.out.println("pool terminated");
            }

        };


         fixed.prestartAllCoreThreads(); // creates all core threads immediately so pool doesn't pay thread creation latency on first task
        // i.e eagerly creates all threads up to corePoolSize. = 3 (eager initialization)

        // above is a good practice sometimes depending on the use case because, if we don't eagerly initialize it then at every new task submitted a new thread will
        // e initialized and for OS thread initialization you know :

/*        JVM + OS
        1. Create new memory
        2. Allocate stack for memory
        3. Ask the OS kernel to start the new native thread
        4. Do internal scheduling setup.*/

        System.out.println("poolSize after prestart: " + fixed.getPoolSize()); // 3


        for (int i = 0; i < 6; ++ i) {
            final int id = i; // capture loop variable for use inside lambda

            fixed.execute( /*runnable task*/ withLoggingAndCleanup(() -> { // execute doesn't return a future
                try {
                    // simulate work by sleeping for a bit; different tasks sleep slightly different amounts
                    Thread.sleep(500 + (id * 50)); // sleep duration depends on task id

                    // intentionally throw on task id == 2 to demonstrate uncaught exception behavior
                    if (id == 2) throw new RuntimeException("boom in task 2 (execute)");
                } catch (InterruptedException e) {
                    // If the thread is interrupted while sleeping, we should handle it
                    System.out.println("task " + id + " interrupted");
                    Thread.currentThread().interrupt();
                }
            }, "task-" + id)); // label passed to wrapper for logging
        }

        // submit() returns a future

        List<Future<?>>futures = new ArrayList<>();

        for (int i = 0; i < 6; ++ i) {
            final int id = i; // capture loop variable for use inside lambda

            futures.add(fixed.submit( /*runnable task*/  withLoggingAndCleanup(() -> { // execute doesn't return a future
                try {
                    // simulate work by sleeping for a bit; different tasks sleep slightly different amounts
                    Thread.sleep(500 + (id * 50)); // sleep duration depends on task id

                    // intentionally throw on task id == 2 to demonstrate uncaught exception behavior
                    if (id == 2) throw new RuntimeException("boom in task 2 (execute)");
                } catch (InterruptedException e) {
                    // If the thread is interrupted while sleeping, we should handle it
                    System.out.println("task " + id + " interrupted");
                    Thread.currentThread().interrupt();
                }
            },     "task-" + id))// label passed to wrapper for logging
            );
        }

        // graceful shutdown.
        fixed.shutdownNow();

        try {
            if (fixed.awaitTermination(5, TimeUnit.SECONDS)) { // if above all task is concluded or will be concluded under 5 sec when we reach if await condition
                // it will directly let it enter else it would wait max of 5 sec and if under this task is completed then let ti enter else execute the else condition
                System.out.println("All task completed gracefully");
            }
            else {
                System.out.println("Taking much longer  force closing pending task...");
                fixed.shutdownNow();
            }
        }
        catch (Exception e) {
            System.out.println("Caught Exception " + e.getMessage());
        }



            List<?> results =  futures
                    .stream()
                    .map( x-> {
                        try {
                            return x.get();
                        } catch (Exception e ) {
                            throw new RuntimeException("Exception caught while fetching task from future " + e.getMessage() );
                        }
                    })
                    .toList();

        // OR

        futures
                .forEach(f -> {
                    try {
                        f.get();  // blocks until task is finished
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                });
        }


        // Start with ExecutorCompletionService and CompletableFuture -







    }
}
