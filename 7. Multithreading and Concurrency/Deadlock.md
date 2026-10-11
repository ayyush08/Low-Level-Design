# Deadlock and Prevention techniques


## What is Deadlock?

A deadlock is a situation in multithreaded  applications where two or more threads are blocked forever, each waiting for the other to release the lock.

Example:  Bank Transfer: Consider two threads, T1 and T2, trying to transfer money between two bank accounts A and B. If T1 locks account A and waits to lock account B, while T2 locks account B and waits to lock account A, both threads will be blocked indefinitely, resulting in a deadlock.

## 4 Coffman Conditions for Deadlock

1. **Mutual Exclusion**: Only one thread can own a resource at a time. 

2. **Hold and Wait**: A thread holds one resource and is waiting for another resource.

3. **No Preemption**: Resources cannot be forcibly taken away from a thread holding them.

4. **Circular Wait**: A closed chain of threads exists, each waiting for a resource to the next.

**For prevention** : Break atleast one of the above conditions.



## Dining Philosophers Problem

5 philosophers are sitting around a table, each with a plate of food. There are 5 forks placed between the plates. Each philosopher needs two forks to eat. If all philosophers pick up the fork on their right at the same time, they will all be waiting for the fork on their left or vice versa, resulting in a deadlock.


# Deadlock Prevention Techniques

## 1. Lock Ordering (breaks Circular Wait)

Ensure that all threads acquire locks in a predefined order, regardless of execution context. This prevents circular wait conditions.


## 2. tryLock with Timeout (breaks Hold and Wait)

## 3. Minimize Nested locks (breaks Hold and Wait)

## 4. Recovery Strategies (more relevant to databases)

- **Wait-Die Scheme**: When a thread request a lock held by another thread, the db accesses the relative priority. If the requesting thread has a higher priority, it waits; otherwise, it is aborted (dies) and restarted later.

- **Wound-Wait Scheme**: If the requesting thread has a higher priority, it will wound (interrupt) the the lower priority thread, forcing it to release the lock. If the requesting thread has a lower priority, it waits.


----

Conclusion:

- **Deadlocks are hard to debug and often happen under load.**
- **Use thread dumps to detect deadlocks in real applications.**