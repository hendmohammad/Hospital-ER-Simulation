public class SemaphoreManager {

    private int permits;

    // Constructor
    public SemaphoreManager(int permits) {

        if (permits < 0) {
            throw new IllegalArgumentException("Permits cannot be negative");
        }

        this.permits = permits;
    }

    // Wait until a permit becomes available, then acquire it
    public synchronized void acquire() throws InterruptedException {
        while(permits == 0) {
            wait();
        }
        
        permits--;
    }

    // Try to acquire a permit immediately
    // Return true if successful, false otherwise
    public synchronized boolean tryAcquire() {
        if (permits > 0) {
            permits--;
            return true;
        } else {
            return false;
        }
    }

    // Try to acquire a permit within a specified time (milliseconds)
    public synchronized boolean tryAcquire(int time) throws InterruptedException {
        
        if (time < 0) {
            throw new IllegalArgumentException("Time cannot be negative");
        }

        long var2 = System.currentTimeMillis() + (long)time; // var2 stores the deadline till the dr. would wait 10:00:00 -> 10:00:03

        while(permits == 0) {
            long var4 = var2 - System.currentTimeMillis(); // calculate the remaining time
            if (var4 <= 0L) {  // if 3 seconds has expired, returns false bec. the doctor couldn't acquire a room 
                return false;
            }

            wait(var4); // dr. wait would wait for the remaining time if 3 secs didn't expire. Exits the loop / Wait ends when there're permits available "notifyAll called" or time ends
        }

        permits--;
        return true;
    }

    // Release a permit and notify waiting threads
    public synchronized void release() {
        permits++;
        notifyAll();
   }
}
