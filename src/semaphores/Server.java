package semaphores;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.LongAdder;

public class Server {

    private final Semaphore semaphore;
    private final LongAdder loginAttempts;
    private static final int NUM_USERS = 100;

    public Server(Semaphore semaphore, LongAdder loginAttempts) {
        this.semaphore = semaphore;
        this.loginAttempts = loginAttempts;
        new Thread(this::printStatus).start();
    }


    private void printStatus() {
        while (true) {
            try {
                Thread.sleep(1000);
                int currentUsers = NUM_USERS - semaphore.availablePermits();
                if (currentUsers == 0) {
                    break;
                }
                var result = "Current users: " + currentUsers +
                        ", login attempts: " + loginAttempts;
                System.out.println(result);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
        System.out.println("Server has serviced all requests! " +
                "The server handled " + loginAttempts.longValue() + " login attempts");
    }

    public boolean tryLogin() {
        loginAttempts.increment();
        return semaphore.tryAcquire();
    }

    public void logout() {
        semaphore.release();
    }
}