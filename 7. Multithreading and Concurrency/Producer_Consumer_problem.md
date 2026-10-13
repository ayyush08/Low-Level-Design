# Producer Consumer Problem

Also known as Banded-buffer problem.

It is a synchronization construct that allows threads to have:
    - **Mutual Exclusion**: Only one thread in critical section at a time.
    - **Wait-notify** coordination between threads.


Here, a Producer thread produces data and puts it into a buffer, while a Consumer thread consumes data from the buffer. The Producer must wait if the buffer is full, and the Consumer must wait if the buffer is empty. The problem and challenge lies in ensuring that producers don't add data when the buffer is full, and consumers don't try to remove data when the buffer is empty - all while avoiding race conditions and ensuring mutual exclusion and proper coordination between threads.

Real life analogy:  A room with one key


Example: 
    Coffee Machine: One producer makes one cup of coffee at a time and One consumer drinks one cup of coffee at a time. 


## Handling Fast Producers and Slow Consumers (Leetcode Judge Example)


With a one-slot buffer, where the producer (coffee machine) and the consumer (customer) worked in perfect alternation — one producing, the other consuming. But real-world systems often face imbalance in processing speeds.


**The Solution**
To fix this, we use a buffered queue (like `Queue<String> submissions = new LinkedList<>();`) that can store multiple submissions at once. This allows:
- Producers to keep submitting even when the consumer is busy.
- Consumers to process submissions at their own pace, pulling from the buffer.