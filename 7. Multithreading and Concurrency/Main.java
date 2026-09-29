import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Without Multithreading
 * **/
// class OrderService{
//     public static void main(String[] args) throws InterruptedException {
//         System.out.println("Placing order...");
//         sendSMS();
//         System.out.println("Task 1 completed");
//         sendEmail();
//         System.out.println("Task 2 completed");
//         String eta = calculateETA();
//         System.out.println("Order Placed. ETA: " + eta);
//         System.out.println("Task 3 completed");
//     }
//     private static void sendSMS() {
//         try {
//             Thread.sleep(2000);
//             System.out.println("SMS sent");
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
//     }
//     private static void sendEmail() {
//         try {
//             Thread.sleep(3000);
//             System.out.println("Email sent");
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
//     }
//     private static String calculateETA() {
//         try {
//             Thread.sleep(5000);
//             return "5 minutes";
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//             return "Error calculating ETA";
//         }
//     }
// }


/**
 * With Multithreading Using Thread Class
 * **/

// class SMSThread extends Thread {
//     public void run() {
//         try {
//             Thread.sleep(2000);
//             System.out.println("SMS sent");
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
//     }
// }

// class EmailThread extends Thread {
//     public void run() {
//         try {
//             Thread.sleep(3000);
//             System.out.println("Email sent");
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
//     }
// }

/*  
    With Multithreading Using Runnable Interface
*/

class SMSThreadRunnable implements Runnable{
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
            System.out.println("SMS sent using Runnable Thread");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class EmailThreadRunnable implements Runnable{
    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            System.out.println("Email sent using Runnable Thread");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ETACalculator implements Callable<String> {
    public final String location;

    public ETACalculator(String location) {
        this.location = location;
    }
    @Override
    public String call() throws Exception {
       System.out.println("[" + Thread.currentThread().getName() + "] Calculating ETA for " + location);
       Thread.sleep(3000);
       return "ETA for " + location + " is 5 minutes";
    }
}

class Main {
 
    public static void main(String[] args) {
        // try {
        //     OrderService.main(args); 
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }

        // SMSThread smsThread = new SMSThread();
        // EmailThread emailThread = new EmailThread();
        // System.out.println("Tasks started...");
        // smsThread.start(); //start() method is used to start the thread and call the run() method
        // System.out.println("Task 1 ongoing");
        // emailThread.start();
        // System.out.println("Task 2 ongoing");

        Thread smsThread = new Thread(new SMSThreadRunnable());
        Thread emailThread = new Thread(new EmailThreadRunnable());
        System.out.println("Tasks started...");
        smsThread.start();
        System.out.println("Task 1 ongoing");
        emailThread.start();
        System.out.println("Task 2 ongoing");

        FutureTask etaTaskRunnable = new FutureTask<>(new ETACalculator("New York"));
        Thread etaThread = new Thread(etaTaskRunnable);

        etaThread.start();


        try {
            smsThread.join(); //join() waits for the SMS thread to complete (die)
            emailThread.join();


            String eta = (String) etaTaskRunnable.get(); //Typecast to String since get() returns Object
            System.out.println(eta);
            System.out.println("All tasks completed");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}