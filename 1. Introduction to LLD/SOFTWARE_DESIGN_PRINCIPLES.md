# Software Design Principles

Software design principles are guidelines that help software developers create systems that are easy to understand, maintain, and extend. These principles can be applied at both the high-level and low-level design stages. Here are three cornerstone key software design principles:


## 1. DRY (Don't Repeat Yourself)

It means that each piece of knowledge or logic should have a single, unambiguous representation within a system. 

### Importance
1. Reduces redundancy and duplication of code.
2. Easier maintenance and updates, as changes need to be made in only one place.
3. Single point of change reduces the risk of introducing bugs.

### How to Apply
 - Identify repetitive code
 - Leverage libraries and frameworks
 - Extract common functionality 
 - Refactor code regularly

### When not to use DRY principle?

1. **Premature Abstraction**: Don't extract common code too early. At first glance, two pieces of code may look similar, but they might change in different ways in the future. Premature abstraction can lead to a rigid design that is hard to change.

2. **Performance critical code**: In some cases, duplicating code can be more efficient than creating a shared function, especially in performance-critical sections of the code.

3. **Sacrificing readability**: Prioritize readability over strict adherence to DRY. If extracting code into a separate function makes the code harder to understand, it may be better to keep it duplicated.

4. **Legace code**: Legacy code may not have tests or documentation, making it risky to refactor. In such cases, introducing DRY principles may lead to unintended consequences.



<br/>
<br/>


## 2. KISS (Keep It Simple, Stupid)

A design should be kept as simple as possible. Complexity should be avoided unless absolutely necessary.

### Importance

1. Easier debugging
2. Improved readability
3. Better maintainability
4. Faster development and deployment

<br/>
<br/>

## 3. YAGNI (You Aren't Gonna Need It)

Always implement things when you actually need them, not when you just foresee that you need them.

### Example
Start with single Payment method, and add more payment methods only when the need arises.

### Importance
1. Reduced waste of time and resources on unnecessary features.
2. Simplified codebase, making it easier to understand and maintain.
3. Faster development cycles, as developers can focus on delivering the most important features first.


### When not to use YAGNI principle?

- Well Known Requirements: If a feature is well-known and expected to be needed in the future, it may be worth implementing it early to avoid future refactoring.

- Performance Optimization: In some cases, implementing a feature early can lead to performance optimizations that would be more difficult to achieve later.