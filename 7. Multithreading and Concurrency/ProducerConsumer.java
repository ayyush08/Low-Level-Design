
// class CoffeMachin{
//     private boolean isCoffeeReady = false;

//     public synchronized void makeCoffee() throws InterruptedException {

//         //While because of "SPURIOUS WAKEUPS",
//         //Java threads can sometimes wake up from wait() without being notified - this is called a spurious wakeup.
//         while(isCoffeeReady){
//             wait(); //wait until coffee is consumed
//         }

//         System.out.println("Making coffee...");
//         Thread.sleep(2000); //simulate time taken to make coffee
//         isCoffeeReady = true;
//         System.out.println("Coffee is ready!");
//         notify(); //notify the consumer that coffee is ready
//     }

//     public synchronized void drinkCoffee() throws InterruptedException {
//         while(!isCoffeeReady){
//             wait(); //wait until coffee is ready
//         }

//         System.out.println("Drinking coffee...");
//         Thread.sleep(1000); //simulate time taken to drink coffee
//         isCoffeeReady = false;
//         System.out.println("Coffee is consumed!");
//         notify(); //notify the producer that coffee is consumed (can also use notifyAll() if multiple producers/consumers)
//     }
// }

// class ProducerConsumer{
//     public static void main(String[] args) {
//         CoffeMachin coffeeMachine = new CoffeMachin();

//         Thread producerThread = new Thread(() ->{
//             while(true){
//                 try {
//                     coffeeMachine.makeCoffee();
//                 } catch (InterruptedException e) {
//                     e.printStackTrace();
//                 }
//             }
//         });

//         Thread consumerThread = new Thread(() ->{
//             while(true){
//                 try {
//                     coffeeMachine.drinkCoffee();
//                 } catch (InterruptedException e) {
//                     e.printStackTrace();
//                 }
//             }
//         });

//         producerThread.start();
//         consumerThread.start();

//     }
// }

//Fast Producer Slow Consumer Example   

import java.util.LinkedList;
import java.util.Queue;

class Submission {
    private static int idCounter = 1;
    private final int submissionId;
    private final String username;

    public Submission(String username) {
        this.submissionId = idCounter++;
        this.username = username;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public String getUsername() {
        return username;
    }
}

class SubmissionQueue {
    private final Queue<Submission> queue = new LinkedList<>();
    private final int MAX_CAPACITY = 5;

    public synchronized void submit(Submission submission) throws InterruptedException{
        while(queue.size() == MAX_CAPACITY){
            System.out.println("Queue is full. " + submission.getUsername() + " is waiting to submit.");
            wait(); //wait until there's space in the queue
        }

        queue.offer(submission);
        System.out.println(submission.getUsername() + " submitted submission ID: " + submission.getSubmissionId());
        notify(); //notify the consumer that a new submission is available
    }

    public synchronized Submission consumer(String judgeName) throws InterruptedException{
        while(queue.isEmpty()){
            System.out.println("Queue is empty. " + judgeName + " is waiting for submissions.");
            wait(); //wait until there's a submission to consume
        }

        Submission submission = queue.poll();
        System.out.println(judgeName + " is reviewing submission ID: " + submission.getSubmissionId() + " from " + submission.getUsername());
        notifyAll(); //notify all producers that space is available in the queue
        return submission;
    }
    
}