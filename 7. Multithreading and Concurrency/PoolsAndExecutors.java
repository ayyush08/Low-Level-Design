import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// class EmailService {
//     private static final ExecutorService executor = Executors.newFixedThreadPool(10); // Thread pool with 10 threads
 

//     public static void sendEmail(String recipient) {
//         executor.execute(() -> {
//             System.out.println("Sending email to " + recipient + " on " + Thread.currentThread().getName());
//             try {
//                 Thread.sleep(1000);  // Simulate delay
//             } catch (InterruptedException e) {
//                 Thread.currentThread().interrupt();  // Handle interruption
//             }
//             System.out.println("Email sent to " + recipient);
//         });
//     }

//     public static void main(String[] args) {
//         for (int i = 1; i <= 25; i++) {
//             sendEmail("user" + i + "@gmail.com");  // Send email to 1000 users
//         }
//         executor.shutdown();  // Gracefully shut down the executor
//     }
// }

class FutureExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
 
        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(1000);
            return 77;
        });
 
        System.out.println("Doing other work...");
 
        Integer result = future.get(); // blocks until result is ready
        System.out.println("Result: " + result);
 
        executor.shutdown();
    }
}