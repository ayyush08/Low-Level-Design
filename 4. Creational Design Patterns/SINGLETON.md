# Singleton Creational Pattern

Let's consider a scenario of a code runner platform where we want analytics about how many times a user has run and submit their code. 


```java
class JudgeAnalytics {
    private int runCount;
    private int submitCount;

    public void incrementRunCount() {
        runCount++;
    }

    public void incrementSubmitCount() {
        submitCount++;
    }

    public int getRunCount() {
        return runCount;
    }

    public int getSubmitCount() {
        return submitCount;
    }
}
class Main {
    public static void main(String[] args) {
        //Imagine this is service A
        JudgeAnalytics analytics1 = new JudgeAnalytics();
        //Imagine this is service B
        JudgeAnalytics analytics2 = new JudgeAnalytics();

        analytics1.incrementRunCount();
        analytics1.incrementSubmitCount();

        System.out.println("Analytics 1 - Run Count: " + analytics1.getRunCount() + ", Submit Count: " + analytics1.getSubmitCount());
        System.out.println("Analytics 2 - Run Count: " + analytics2.getRunCount() + ", Submit Count: " + analytics2.getSubmitCount());
    }
}
```

Now in the above scenario, we have two different services (service A and service B) that are creating their own instances of `JudgeAnalytics`. This means that the run count and submit count will be maintained separately for each instance, which is not what we want. We want a single instance of `JudgeAnalytics` that can be shared across the entire application so that analytics can be tracked globally.

Singleton pattern is a design pattern that solves something like this. 

## Definition

The singleton pattern ensures that a class has only one instance throughout the lifecycle of an application and provides a global point of access to that instance.

## Why do we need Singleton?

- **Global Access**: Singleton provides a way to access its instance globally. This is useful when you want to have a single point of access for certain resources or services.

- **Resource Control**: Singleton can be used to control access to resources that are shared across the application, such as database connections, configuration settings, or logging services.

- **Logical Reasoning**: In some cases, it makes sense to have a single instance of a class to represent a concept or entity in the application. For example, a configuration manager or a logging service.

- **Access from Multiple Places**: Singleton allows access to its instance from multiple places in the application, making it easier to manage shared state or resources.


### Prime Examples:

Database connection pool, logging service, configuration manager, thread pool manager, etc.

## How to implement Singleton?

## 1. Eager Loading

In Eager Loading, the Singleton instance is created as soon as the class is loaded, regardless of whether it's ever used. 

```java
class JudgeAnalytics {
    private static final JudgeAnalytics instance = new JudgeAnalytics();
    
    private JudgeAnalytics() {
        // private constructor to prevent instantiation
    }

    public static JudgeAnalytics getInstance() {
        return instance;
    }
}
class Main {
    public static void main(String[] args) {
        JudgeAnalytics analytics1 = JudgeAnalytics.getInstance();
        JudgeAnalytics analytics2 = JudgeAnalytics.getInstance();
        
        System.out.println("Are both instances the same? " + (analytics1 == analytics2)); // true
    }
}
```

Now here both `analytics1` and `analytics2` are pointing to the same instance of `JudgeAnalytics`, ensuring that the values or whatever state is maintained globally across the application.

- **Pros**: Simple to implement, thread-safe without synchronization overhead.
- **Cons**: Instance is created even if it might not be used, which can lead to resource wastage. Not suitable for heavy objects or objects that require a lot of resources to create.


## 2. Lazy Loading

In Lazy Loading, the Singleton instance is created only when it's needed - the first time the getInstance() method is called.

```java
class JudgeAnalytics {
    private static JudgeAnalytics instance;
    
    private JudgeAnalytics() {
        // private constructor to prevent instantiation
    }

    public static JudgeAnalytics getInstance() {
        if (instance == null) {
            instance = new JudgeAnalytics();
        }
        return instance;
    }
}
```

Now here, the instance of `JudgeAnalytics` is created only when `getInstance()` is called for the first time. This can save resources if the instance is never needed.

- **Pros**: Saves resources by creating the instance only when needed.
- **Cons**: Not thread-safe. If multiple threads call `getInstance()` simultaneously, it can lead to multiple instances being created which requires additional synchronization mechanisms to ensure thread safety.


## Thread Safety : A Concern with Singleton Pattern

In a single-threaded environment, implementing a Singleton is straightforward. However, things get complicated in multi-threaded applications, which are very common in modern software (especially web servers, mobile apps, etc.).


## The Problem

Let's say two threads, Thread A and Thread B, both call `getInstance()` at the same time when the instance is null. Both threads will check if `instance` is null, find that it is, and then both will create a new instance of `JudgeAnalytics`. This results in two different instances being created, which violates the Singleton pattern.

This kind of bug is hard to detect,severe and can be costly.

## Different Approaches to Ensure Thread Safety

Apart from naive Eager Loading, we can use the following approaches to ensure thread safety in Singleton implementation:

### 1. Synchronized Keyword

```java
class JudgeAnalytics {
    private static JudgeAnalytics instance;
    
    private JudgeAnalytics() {
        // private constructor to prevent instantiation
    }

    public static synchronized JudgeAnalytics getInstance() {
        if (instance == null) {
            instance = new JudgeAnalytics();
        }
        return instance;
    }
}
```

Here, we have added the `synchronized` keyword to the `getInstance()` method. This ensures that only one thread can execute this method at a time, preventing multiple instances from being created.

### 2. Double-Checked Locking

```java
class JudgeAnalytics {
    private static volatile JudgeAnalytics instance;
    
    private JudgeAnalytics() {
        // private constructor to prevent instantiation
    }

    public static JudgeAnalytics getInstance() {
        if (instance == null) { 
            synchronized (JudgeAnalytics.class) { 
                if (instance == null) { 
                    instance = new JudgeAnalytics();
                }
            }
        }
        return instance;
    }
}
```

Here, we first check if the instance is null without locking. If it is null, we then synchronize and check again before creating the instance. This reduces the overhead of acquiring a lock every time `getInstance()` is called.

### 3. Bill Pugh Singleton Implementation (Java 5+)

```java
class JudgeAnalytics {
    private JudgeAnalytics() {
        // private constructor to prevent instantiation
    }

    private static class Holder {
        private static final JudgeAnalytics INSTANCE = new JudgeAnalytics();
    }

    public static JudgeAnalytics getInstance() {
        return Holder.INSTANCE;
    }
}
```

Pretty similar to eager loading, but the instance is inside an inner class which is not loaded until the `getInstance()` method is called. This ensures that the instance is created only when needed and also provides thread safety without synchronization overhead.


## Pros of Singleton Pattern

- **Clean and Simple**: The Singleton pattern is easy to understand and implement. It provides a clear structure for ensuring a single instance of a class.

- **One instance guarantee**: The Singleton pattern guarantees that there is only one instance of the class, which can be useful for managing shared resources or state.

- **Global access**: The Singleton pattern provides a way to have maintain global resources and access them from anywhere in the application.

- **Supports Lazy Initialization**: The Singleton pattern can be implemented in a way that allows for lazy initialization, meaning the instance is created only when it is needed, which can save resources.

## Cons of Singleton Pattern

- **Confused with Factory**: When a Singleton class requires parameters for instantiation, it may blur lines with the Factory pattern, leading to design confusion.

- **Difficult to Test**: Since the Singleton holds a global state, it becomes difficult to isolate and mock for unit testing, thus potentially hindering testability.

- **High Coupling**: Classes that depend on a Singleton are tightly coupled to it, which can make the code less flexible and harder to maintain.

- **Race conditions**: Something like a database connection where multiple threads are trying to modify a single value at the same time can lead to race conditions

- **Violates Single Responsibility Principle**: The Singleton pattern can lead to a class having multiple responsibilities, such as managing its own instance and providing its functionality, which can violate the Single Responsibility Principle.