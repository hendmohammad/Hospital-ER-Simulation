public class SemaphoreManager {
   private int permits;

   public SemaphoreManager(int var1) {
      this.permits = var1;
   }

   public synchronized void acquire() throws InterruptedException {
      while(this.permits <= 0) {
         this.wait();
      }

      --this.permits;
   }

   public synchronized boolean tryAcquire() {
      if (this.permits > 0) {
         --this.permits;
         return true;
      } else {
         return false;
      }
   }

   public synchronized boolean tryAcquire(int var1) throws InterruptedException {
      long var2 = System.currentTimeMillis() + (long)var1;

      while(this.permits <= 0) {
         long var4 = var2 - System.currentTimeMillis();
         if (var4 <= 0L) {
            return false;
         }

         this.wait(var4);
      }

      --this.permits;
      return true;
   }

   public synchronized void release() {
      ++this.permits;
      this.notify();
   }
}
