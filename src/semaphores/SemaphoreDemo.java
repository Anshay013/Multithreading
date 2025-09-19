package semaphores;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

public class SemaphoreDemo {

    private static final int NUM_REQUESTS = 1000;

    public static void main(String[] args) {

            final Server server = new Server(new Semaphore(100), new LongAdder());
            final Random random = new Random();
            try (var executor = Executors.newFixedThreadPool(NUM_REQUESTS)) {
                for (int i = 0; i < NUM_REQUESTS; i++) {
                    executor.submit(() -> {
                        try {
                            while (!server.tryLogin()) { // if server login is false keep on attempting, so login attempts we print is so huge. Since semaphore has
                                                        // mo available permit for most of login attempts. Also make the thread sleep for 1 sec. i.e if login attempt fails
                                                      // wait for 1 sec then reattempt.
                                Thread.sleep(random.nextInt(1000));
                            }
                            Thread.sleep(random.nextInt(1000));
                            server.logout();
                        } catch (InterruptedException e) {
                            throw new RuntimeException();
                        }
                    });
                }
            }
        }

    }

