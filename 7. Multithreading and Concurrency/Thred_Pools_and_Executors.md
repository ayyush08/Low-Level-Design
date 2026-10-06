# Thread Pools and Executors

## Why not to create threads manually in a real-world system?

Let's say you are building a ride-matching system like Uber.
Every time a new ride request comes in, you create a new thread to handle the request and match it with a driver.

```java
class RideMatchingService {
    public void requestRide(String riderId) {
 
        // Creating a new thread for the ride
        Thread matchThread = new Thread(() -> {
            System.out.println("Matching rider " + riderId + " to a driver...");
            // Simulate some processing
            try {
                Thread.sleep(1000); // Simulate a 1-second matching process
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Ride matched for rider " + riderId);
        });
        matchThread.start();
    }
    class Main {
    public static void main(String[] args) {
        RideMatchingService rideService1 = new RideMatchingService();
        RideMatchingService rideService2 = new RideMatchingService();
 
        rideService1.requestRide("Raj");
        System.out.println("task1 running...");
 
        rideService2.requestRide("John Doe");
        System.out.println("task2 running...");
    }
}
}
```

Now, if you have 1000 ride requests coming in at the same time, you will create 1000 threads. This can lead to a  lot of problems:

1. **Thread Explosion**: OS cannot handle so many requests at the same time. It can lead to resource exhaustion and system crashes.

2. **Memory Issues**: Each thread consumes memory for its stack (~1mb by default)

3. **Thread Leaks**: Created but never terminated properly threads can lead to memory leaks and resource exhaustion.

4. **Context Switching Overhead**: With too many threads, the CPU spends more time switching between threads than executing them.


### Better Approach: Use Thread Pools

Use a pool of worker threads that are reused to handle multiple tasks. 

Real life analogy: Instead of hiring multiple chefs for each order in a restaurant, you hire a few chefs in a team to fulfill all the orders. This way, you can handle multiple orders without hiring too many chefs.

## Java Executors Framework

It's a java framework,  a high-level replacement for manually managing threads.
It decouples:
    - Task Submission (What you want to do)
    - Task Execution (How you want to do it)

Use newFixedThreadPool() to create a thread pool with a fixed number of threads. The number of threads can be equal to the number of CPU cores available.

```java
class EmailService {
    private static final ExecutorService executor = Executors.newFixedThreadPool(10); // Thread pool with 10 threads
 

    public static void sendEmail(String recipient) {
        executor.execute(() -> {
            System.out.println("Sending email to " + recipient + " on " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);  // Simulate delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Handle interruption
            }
            System.out.println("Email sent to " + recipient);
        });
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 25; i++) {
            sendEmail("user" + i + "@gmail.com");  // Send email to 1000 users
        }
        executor.shutdown();  // Gracefully shut down the executor
    }
}
```

In the above example, we created a thread pool with 10 threads. When we submit 25 email tasks, the executor will manage the execution of these tasks using the available threads in the pool. Only 10 tasks will run concurrently, and the rest will wait in the queue until a thread becomes available. This approach prevents thread explosion and optimizes resource usage.


## Method to Submit Tasks

```java
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
```

1. `executor.execute(()-> Runnable task)`: Submits a Runnable task for execution. Fire-and-forget approach. No result is returned.

2. `executor.submit(()-> Callable task)`: Submits a Callable task for execution. Returns a Future object of the defined type. You can use the Future object to retrieve the result of the task once it is completed. Useful when you want to track,cancel or return the result of the task.

## Shutdown Methods

- `executor.shutdown()`: Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted. The executor will terminate once all tasks have completed. (waits for pending tasks to complete without accepting new tasks)

- `executor.shutdownNow()`: Attempts to stop all actively executing tasks, halts the processing of waiting tasks, and returns a list of the tasks that were waiting to be executed. `List<Runnable>` is the return type of this method. (forces shutdown immediately, may interrupt running tasks)


-------


## Thread Starvation and Fairness

- **Thread Starvation**: It occurs when long running tasks hog/hold the CPU and prevent smaller time consuming tasks from getting executed. This can lead to smaller tasks waiting indefinitely for CPU time.

**Fix**: Use priority queues or separate thread pools for long running tasks and smaller tasks.



## When to use Fixed, Cached or Scheduled Thread Pools?


### Fixed Thread Pool

A Fixed Thread Pool creates a pool with a fixed number of threads. Once a task is submitted, the executor assigns it to an available thread from the pool. If all threads are busy, new tasks are queued until a thread becomes available.

**Use Case**: Applications where the number of tasks is known in advance, and the system should process a fixed number of concurrent tasks (e.g., handling a fixed number of user requests simultaneously).

#### Advantages:

- **Predictable Resource Usage**: The number of threads is fixed, which helps in managing system resources effectively.

- **Better Control**: You can control the maximum number of concurrent tasks, preventing resource exhaustion.

#### Disadvantages:

- **Limited Scalability**: If the number of tasks exceeds the number of threads, tasks will be queued, potentially leading to delays.

- **Underutilization**:  If fewer tasks are available than the number of threads, some threads may remain idle, wasting system resources.


### Cached Thread Pool

A Cached Thread Pool creates new threads as needed but reuses previously constructed threads when they are available. If a thread remains idle for more than 60 seconds, it is terminated and removed from the pool.

**Use Case**: Short-lived tasks that are executed intermittently/frequently,  such as handling burst traffic or processing small background tasks where thread usage is unpredictable.

#### Advantages:

- **Scalable**: Threads are created dynamically and pool can grow as needed to handle the burst of tasks.

- **Efficient Resource Usage**: Threads are reused whenever possible, reducing the overhead of thread creation and destruction.

#### Disadvantages:

- **Potential for Thread Explosion**: If tasks are submitted at a high rate and threads are not reused quickly enough, it can lead to a large number of threads being created, which may exhaust system resources.

- **Less Predictable Resource Usage**: The number of threads can grow significantly, making it harder to predict resource usage and manage system performance.


### Scheduled Thread Pool

A Scheduled Thread Pool allows you to schedule tasks with fixed-rate or fixed-delay execution policies. It supports delayed or periodic execution of tasks, making it useful for scheduling tasks at regular intervals or after a specific delay.

```java
class SessionCleaner {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> System.out.println("Cleaning up expired sessions...");

        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
    }
}
```

In the above example, a session-cleaning task is scheduled to run every 5 seconds, starting immediately (initialDelay = 0). This is achieved using scheduleAtFixedRate(), which ensures periodic execution using a ScheduledExecutorService.

**Use Case**: Periodic tasks such as periodic data synchronization, background maintenance tasks, or any task that needs to run at regular intervals.

#### Advantages:

- **Task Scheduling**: Allows for precise scheduling of tasks, making it suitable for time-sensitive operations.

- **Flexible** : Provides options for both fixed-rate and fixed-delay execution, allowing developers to choose the appropriate scheduling strategy based on their requirements.

#### Disadvantages:

- **Less efficient for short-lived tasks**: If the scheduled tasks are short-lived and frequent, using ScheduledThreadPool is not ideal because such tasks might not even need to be executed periodically, and the overhead of scheduling may outweigh the benefits.

- **Thread management overhead**: Managing scheduled tasks can introduce additional complexity and overhead of tracking execution time and delay intervals.