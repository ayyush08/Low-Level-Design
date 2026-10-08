/* Race Condition Counter Example */

// class PurchaseCounter{
//     private int count = 0;

//     public void increment(){
//         count++;
//     }

//     public int getCount(){
//         return count;
//     }
// }

// class RaceConditionDemo{
//     public static void main(String[] args) {
//         PurchaseCounter counter = new PurchaseCounter();

//         Runnable task = () -> {
//             for(int i=0; i<10000; i++){
//                 counter.increment();
//             }
//         };

//         Thread thread1 = new Thread(task);
//         Thread thread2 = new Thread(task);

//         thread1.start();
//         thread2.start();

//         try {
//             thread1.join();
//             thread2.join();
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }

//         //Expecting the final count to be 2000, but due to race condition, it may not be
//         System.out.println("Final count: " + counter.getCount());
//     }
// }




/* Synchronized Method Example */
// class PurchaseCounterSyncMethod{
//     private int count = 0;

//     public synchronized void increment(){
//         count++;
//     }

//     public int getCount(){
//         return count;
//     }
// }

// class RaceConditionSyncMethodDemo{
//     public static void main(String[] args) {
//         PurchaseCounterSyncMethod counter = new PurchaseCounterSyncMethod();

//         Runnable task = () -> {
//             for(int i=0; i<10000; i++){
//                 counter.increment();
//             }
//         };

//         Thread thread1 = new Thread(task);
//         Thread thread2 = new Thread(task);

//         thread1.start();
//         thread2.start();

//         try {
//             thread1.join();
//             thread2.join();
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }

//         //With synchronized method, the final count should be 20000
//         System.out.println("Final count: " + counter.getCount());
//     }
// }



/* Synchronized Block Example */

// class PurchaseCounterSyncBlock{
//     private int count = 0;

//     public void increment(){
//         synchronized(this){
//             count++;
//         }
//     }

//     public int getCount(){
//         return count;
//     }
// }

// class RaceConditionSyncBlockDemo{
//     public static void main(String[] args) {
//         PurchaseCounterSyncBlock counter = new PurchaseCounterSyncBlock();

//         Runnable task = () -> {
//             for(int i=0; i<10000; i++){
//                 counter.increment();
//             }
//         };

//         Thread thread1 = new Thread(task);
//         Thread thread2 = new Thread(task);

//         thread1.start();
//         thread2.start();

//         try {
//             thread1.join();
//             thread2.join();
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }

//         //With synchronized block, the final count should be 20000
//         System.out.println("Final count: " + counter.getCount());
//     }
// }


/* Volatile Keyword Example */

// class PurchaseCounterVolatile{
//     private volatile int count = 0;

//     public void increment(){
//         count++;
//     }

//     public int getCount(){
//         return count;
//     }
// }

import java.util.concurrent.atomic.AtomicInteger;

class PurchaseAtomicCounter{
    private AtomicInteger count = new AtomicInteger(0);

    public void incrementLikes(){
        int prev,next;
        do{
            prev = count.get();
            next = prev + 1;
        } while(!count.compareAndSet(prev, next));
    }

    public int getCount(){
        return count.get();
    }
}