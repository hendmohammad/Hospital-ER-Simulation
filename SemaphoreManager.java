public class SemaphoreManager {

    private int permits;

    // Constructor
    public SemaphoreManager(int permits) {

    }

    // Blocking acquire
    public synchronized void acquire() throws InterruptedException {

    }

    // Non-blocking acquire
    public synchronized boolean tryAcquire() {

        return false;
    }

    // Timed acquire
    public synchronized boolean tryAcquire(int time)
            throws InterruptedException {

        return false;
    }

    // Release a permit
    public synchronized void release() {

    }
}
