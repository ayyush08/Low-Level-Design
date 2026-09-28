# Multithreading & Concurrency


# Program, Process & Thread

## Program

A program is an executable file that contains a set of instructions to perform a specific task.

Ex: `chrome.exe` - A program that allows users to browse the internet.

## Process

A process is an executing instance of a program. It is a program in execution and has its own memory space,program counter, system resources, and execution context.

Ex: Multiple windows of `chrome.exe` running simultaneously, each representing a separate process.

## Thread

Thread is a smallest executable unit of a process. Threads are sub-tasks of a process that can run concurrently. All threads can access shared memory space of the process, but each thread has its own stack and registers.

Ex: Loading UI, calling network requests, and handling user input in a web browser can be done using multiple threads within the same process.

# What are cores in CPU?

A core is a single processing unit inside a CPU - capable of executing instructions independently. 

It is like a mini-CPU within the CPU. 

Over the years, CPU manufacturers have increased the number of cores in a CPU to improve performance and multitasking capabilities.

**One thread at a time per core**: A single core can execute one thread at a time. However, modern CPUs support more with Hyper-Threading, intelligent time-slicing, and resource sharing.


# What is context switching?

Context switching is the process of storing the state of a currently running thread/Process and switching to another thread. 

## How does context switching happen?

1. The CPU saved the current state of the running thread's context
2. Loads the context of the next thread to be executed
3. Resumes execution of the new thread

Switching between threads is managed by **Thread Scheduler** in the operating system.

- Disadvantage of context switching: It takes time to save and load the context, which can lead to performance overhead, specially when high number of threads are involved for fewer CPU cores.


# What is Multithreading? Why do we need it?

Multithreading is the ability of a CPU to run multiple threads (independent tasks) concurrently, either truly in parallel (on multiple cores) or via context switching (on a single core).

Each thread:
- Can run independently
- Can share the same memory space of the process
- Performs a specific task within the process

## Why use Multithreading?

- Better performance
- Non-blocking: Threads can run concurrently, allowing one thread to perform a task while another thread is waiting for I/O operations to complete.
- Resource sharing
- Scalability in backend services.

Example: Uber -> trip matching , ETA calculation, pricing - all can be done in parallel using multithreading, which improves the overall performance of the application.

# Concurrency Vs Parallelism

| Aspect | Concurrency | Parallelism |
|---|---|---|
| Definition | Multiple tasks make progress over time, but not necessarily at the same time. | Multiple tasks run at the exact same time on multiple cores. |
| Cores | Can work with one core. | Requires multiple cores. |
| Execution | Tasks appear to run at the same time but are actually interleaved. | Tasks actually run at the same time, each on its own core. |
| Focus | Structure: how to do many things. | Execution: how to finish many things at the same time. |


# Process vs Thread

| Process | Thread |
|---|---|
|Independent program in execution | Sub-unit of a process |
| Has its own memory space | Shares memory space of the process with other threads. |
| Fully isolated from other processes | Can communicate with other threads of the same process easily |
| Communication is complex (IPC, sockets, etc.) | Communication is simple (shared memory) |
| Heavy overhead | Lightweight overhead |
| One process crash does not affect other processes | One thread crash may affect other threads in the same process. |
| Example: PostgresSQL | Example: Chrome tab, uber backend service, etc. |


# When to use Threads?

- Tasks need to share data.
- Low overhead is important.
- Tasks are part of same logic.
- High Performance is required.
- Tightly coupled behaviour is needed between tasks.
- Responsiveness is Key (e.g., UI applications).

# When to use Processes?

- Tasks require isolation.
- One crash should not affect other tasks.
- Security boundaries are needed.
- Different tech stack.
- Resource limits need to be enforced.
- Used by different users (e.g., multi-user systems).


# Fault Tolerance & Isolation

## Fault Tolerance

It is the ability of the system to continue functioning even when some of its components fail.

It detects, contains and recovers from failures, without affecting the user experience.

Real life example: A plane - one function of the system may fail, but the plane can still operate safely.

**Redundancy** - The code might be redundant in order to provide fault tolerance. For example, a system may have multiple servers running the same service, so if one server fails, the others can take over.

**Graceful degradation** - The system may continue to operate at a reduced level of functionality when some components fail. For example, a website may still allow users to browse content even if the search functionality is down.

**Self-healing** - The system may automatically detect and recover from failures without human intervention. For example, a cloud service may automatically restart a failed server instance.

**Error-containment** - The system should contain its errors to itself and not expose them to other parts of the system or the client. For example, a database may roll back a failed transaction to prevent data corruption.

## Isolation

Isolation means keeping different components or tasks independently from each other so that actions or failures in one component do not affect the others.

Design strategy to ensure that the components are sand-boxed from each other.

**Memory Separation** - The tasks cannot be sharing same memory because one task can corrupt the memory of another task. For example, in a multi-process architecture, each process has its own memory space.

**Failure containment** - The tasks should be isolated in such a way that if one task fails, it does not affect the other tasks. For example, in a microservices architecture, if one service fails, it should not bring down the entire system.

**Security Boundaries** - The tasks should be isolated to prevent unauthorized access or data leakage. For example, in a multi-tenant system, each tenant's data should be isolated from other tenants.

**Predictable behaviour** - The tasks should be isolated to ensure that they behave predictably and do not interfere with each other. For example, in a real-time system, one task should not be able to block or delay the execution of another task.




