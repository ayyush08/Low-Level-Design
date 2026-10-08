# Thread Safety and Synchronization

## What is Thread Safety?

Thread safety means that a piece of code, object or method behaves correctly and predictably when accussed by multiple threads at the same time , without corrupting data or producing incorrect results.

Thread safety ensures correctness under concurrent access.

Example:

**Purchase Counter:**

Imagine a sale is going live and a counter is tracking the number of purchases made. If multiple threads are trying to update the counter at the same time (2 users clicked purchase at the same time) - both may see the same count and increment it incorrectly, leading to an inaccurate count of purchases. 

This is where thread safety comes into play. By ensuring that the counter is updated in a thread-safe manner, we can prevent race conditions and ensure that the count reflects the actual number of purchases made.



## Fix 1 : Synchronized keyword

**Concept:** The `synchronized` keyword acquires a object monitor lock , allowing only one thread at a time.

**Fix it via:**

1. **Method Level Synchronization:* Simpler but blocks the whole method into a single thread and others has to wait for the lock to be released.

2. **Block Level Synchronization:** More fine-grained control, blocks only the critical section of code, allowing other threads to execute non-critical sections concurrently.



### What is a Monitor Lock?

- Every object in Java has an intrinsic lock (or monitor lock) associated with it. 
- The `synchronized` keyword acquires the lock on that object
- Only one thread can hold the lock at a time. If another thread tries to acquire the lock, it will be blocked until the lock is released.




## Fix 2 : Volatile Keyword

- Ensures visibility and not atomicity (single operation).

- It ensures that the latest value of a variable is always read by all threads. (count++ is not atomic, but all threads will see the latest value of count straight out of main memory)

- **Use only when one thread writes and other threads read the variable.**

#### Core Gurantees of Volatile:

1. **Visibility:** Changes made by one thread to a volatile variable are visible to all other threads immediately.
2. **No Caching:** : Value is always read from and written to main memory (not the cached copy in CPU Register)/
3. **No Atomicity:** Operations on volatile variables are not atomic. 


## Atomic Variables (Atomic Integer, Atomic Boolean)


Atomic variables are part of the `java.util.concurrent.atomic` package and provide a way to perform atomic operations on single variables without using synchronization.

They use **CAS (Compare-And-Swap)** method at hardware level. It is *lock-free* and **highly performant**.

**CAS Concept:**
    - Think of it like "If value is what I expect it to be, then change it to a new value; otherwise, do nothing."
    - Prevents Race conditions without locking.


#### Pros
 - High-Performance
 - Non-blocking

#### Cons
 - May fall into high contention (too many retries)



### What enables the Atomicity of Atomic Variables?

The magic is in the hardware-level lock-free instructions and java's unsafe class under the hood. 

Only one thread can win and this is guaranteed by the atomicity of the CAS operation at the hardware level. If a thread fails to update the value, it will retry until it succeeds.



| Feature | `synchronized` | `volatile` | `AtomicInteger` |
|---|---|---|---|
| Guarantees Atomicity | ✅ | ❌ | ✅ |
| Guarantees Visibility | ✅ | ✅ | ✅ |
| Blocking | ✅ | ❌ | ❌ |
| Performance | Lower | High | High |
| Use Case | Complex operations | One writer, many readers | Simple counters or flags |