# Single Responsibility Principle (SRP)

A class should have only one reason to change. This means that a class should only have one job, one responsibility , one purpose.

If a class has more than one responsibility, these responsibilities become coupled and changing one might break the other. This makes the class harder to understand and maintain.


## Real Life Analogy (Restaurant)

A restaurant has many responsibilities: cooking food, serving customers, cleaning tables, and managing finances. If one person is responsible for all of these tasks, it becomes difficult to manage and maintain the restaurant effectively.


## Why SRF matters ?

Let's take an example of **Leetcode Code Compiler**:

Currently, the code compiler has following responsibilities:

- Adds driver code
- Performs syntax checking
- Runs code with already fed test cases
- Stores output in Database
- Returns necessary output to the user

Now implementing all these responsibilities in a single class **LeetcodeCompiler** would violate the SRP. 

Instead, we can break down the responsibilities into separate classes:

- **DriverCodeAdder**: Responsible for adding driver code to the user's code.
- **SyntaxChecker**: Responsible for checking the syntax of the code.
- **TestRunner**: Responsible for running the code with test cases.
- **DatabaseManager**: Responsible for storing the output in the database.
- **OutputRetriever**: Responsible for returning the necessary output to the user.

Another class **Coordinator** can be created to coordinate the above classes and manage the flow of the code compilation process.

By following the SRP, each class has a single responsibility, making the code easier to understand, maintain, and modify in the future without affecting other parts of the system.

## Advantages/Benefits of SRP

1. **Improved Maintainability**: When a class has a single responsibility, it is easier to understand and modify without affecting other parts of the system.

2. **Enhanced Readability**: Classes with a single responsibility are easier to read and comprehend, making it simpler for developers to work with the code.

3. **Better Reusability**: Classes with a single responsibility can be reused in different contexts without introducing unintended side effects.

4. **Facilitates Testing**: Testing becomes more straightforward when classes have a single responsibility, as it is easier to isolate and test individual components.

5. **Lower Risk in Changes**: When a class has a single responsibility, changes made to that class are less likely to impact other parts of the system, reducing the risk of introducing bugs.


## Common mistakes while violating SRP

1. **Mixing Database logic with business logic**: A class that handles both database operations and business logic can become difficult to maintain and test.

2. **Combining UI code with business logic**: A class that handles both user interface rendering and business logic can lead to tight coupling and make it harder to modify the UI without affecting the underlying logic.

# IS SRP just for classes ?

The answer is no. 
 SRP can be applied to methods, modules, microservices and even entire systems. 
 The key is to ensure that each component has a single responsibility and that changes in one area do not affect others unnecessarily.
Hence, SRP is not just for classes. It's a mindset you can apply from the smallest method to the largest system design.

