# Locks and Synchronization Mechanisms


## What is the problem?

Why not use `synchronized` keyword to solve race conditions? The `synchronized` keyword is a simple way to ensure that only one thread can access a block of code at a time. However, it has some limitations:

    - no timeout support (if user goes idle, the thread will wait indefinitely)
    - no explicit control (can't do anything outside of the synchronized block)
    - can't intercept a waiting thread 
    - no fairness guarantee (if multiple threads are waiting, there's no guarantee which one will get the lock next)



## Locks v/s Mutex

| Lock | Mutex |
|---|---|
| A general synchronization mechanism used to control access to a shared resource. | A specific type of lock used to achieve mutual exclusion, allowing only one thread to access a resource at a time. |
| A lock may or may not enforce ownership depending on its implementation. In Java, both `synchronized` and `ReentrantLock` are ownership-bound. | A mutex strictly follows ownership semantics, meaning only the thread that acquired it should release it. |
| In Java, a monitor lock acquired using `synchronized` is automatically released when the same thread exits the synchronized block or method. | In Java, `ReentrantLock` behaves like a mutex because only the owning thread can call `unlock()` successfully. |
| Locks are a broader concept and can include monitors, reentrant locks, read-write locks, spin locks, and other synchronization mechanisms. | A mutex is mainly focused on exclusive ownership, where one thread enters the critical section, and others must wait. |
| Real-life analogy: A general access-control system, like a door lock used to protect a room. | Real-life analogy: A key-based room where only the person who took the key is expected to return it. |


## Reentrant Lock

Typically used for fine-grained control and fairness across threads. 

Use it when you want:
 - Explicit lock acquisition and release (using `lock()` and `unlock()` methods).
 - To try acquiring a lock with a timeout (using `tryLock()` method).
 - Fine-grained control without the `synchronized` keyword.



### tryLock() 

- tries to acquire the lock immediately.
- If the lock is available, it acquires the lock and returns `true`.
- If the lock is not available, it returns `false` immediately without blocking the thread.

You can also specify tryLock(timeout, TimeUnit) to wait for a specified time before giving up.


## Read Write Lock

When a data structure is read-heavy and write operations are infrequent (e.g., stock quotes, configuration caches, product catalogs), blocking every reader while one writer updates a single value wastes parallelism.

ReadWriteLock solves this by giving us two independent locks:
    - **Read lock** – many threads can hold it simultaneously as long as no thread is writing.
    - **Write lock** – exclusive; once held, it blocks all other readers and writers.


## Semaphores


It is a concurrency control primitive that is used to limit the number of threads that can access a shared resource concurrently. 

Real world problem: Account sharing in Netflix, Disney+, etc.

Solution: A tool that controles concurrent access to a resource and limits access to N users/devices at a time.


### What is a semaphore?

- Think of it as a token bucket. 
- It has a very fixed number of permits.
- A thread can only proceed if it acquires a token.
- Once done, it releases the token back to the bucket for other threads to use.
- `acquire()` , `release()` , `tryAcquire()` .



## Monitors v/s Reentrant Locks v/s Semaphores


| Feature | Monitor (`synchronized`) | `ReentrantLock` | `Semaphore` |
|---|---|---|---|
| Type | Intrinsic | Explicit Lock API | Permit-based access |
| Concurrency Limit | 1 thread | 1 thread | N threads |
| Reentrant | ✅ | ✅ | ❌ |
| Timeout Support | ❌ | ✅ | ✅ |
| Wait/Notify | ✅ (`wait()`/`notify()`) | ✅ (`Condition.await()`) | ❌ |
| Ownership Semantics | Thread-bound | Thread-bound | No ownership (permits only) |
| Best Use Case | Simple mutual exclusion | Fine-grained locking | Limiting concurrent access |
| Example Use Case | Producer-Consumer | BMS ticket booking | DB connection pool |



----

As we conclude, it's important to understand that all these tools - whether it's a simple monitor, a reentrant lock, or a semaphore - serve one underlying purpose: **safe coordination in a concurrent world**. These aren't just utilities to pick and choose from casually.

In reality, **synchronization is not optional - it's infrastructure**. It's what keeps threads from colliding, ensures data stays consistent, and allows systems to scale without breaking. Much like electricity powers everything silently in the background, synchronization quietly powers every reliable multithreaded application.