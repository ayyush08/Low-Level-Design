# Dependency Injection

Let us consider the following example:

```java
class OrderService{
    private InventoryService inventoryService = new InventoryService();
    private PaymentService paymentService = new RazorpayPaymentService();
    private NotificationService notificationService = new NotificationService();

    public void checkout(Order order){
        inventoryService.blockItems(order);
        paymentService.process(order);
        notificationService.sendConfirmation(order);
    }
}
```

**Problems with the above code:**

1. **Hardcoded Logic**: The `OrderService` class is tightly coupled with specific implementations of `InventoryService`, `PaymentService`, and `NotificationService`. This makes it difficult to change the behavior of the `OrderService` without modifying its code.

For example, if we want to switch from `RazorpayPaymentService` to `StripePaymentService`, we would have to manually change the code everywhere `RazorpayPaymentService` is used (OCP violation).

2. **Testing Challenges**: Since the dependencies are hardcoded, it becomes challenging to test the `OrderService` class in isolation. 

For example, if we want to test the `checkout` method, we would have to use the actual implementations of `InventoryService`, `PaymentService`, and `NotificationService`, which may involve external systems (like payment gateways) and make the tests slow and unreliable.

3. **Scalability Issues**: As the application grows, the number of dependencies may increase, leading to a more complex and less maintainable codebase. Each time a new service is added or an existing one is modified, it may require changes in multiple places, increasing the risk of introducing bugs.



## Definition

Dependency Injection (DI) is a design pattern in which an object receives its dependencies from an external source rather than creating them itself. This promotes loose coupling between classes and makes the code more modular, testable, and maintainable.

In simpler terms, you don't `new()` your dependencies inside a class; you inject it.


## Implementation

```java

class OrderService{
    private InventoryService inventoryService;
    private PaymentService paymentService;
    private NotificationService notificationService;

    public OrderService(InventoryService inventoryService, PaymentService paymentService, NotificationService notificationService){
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }

    public void checkout(Order order){
        inventoryService.blockItems(order);
        paymentService.process(order);
        notificationService.sendConfirmation(order);
    }
}

class Main{
    public static void main(String[] args) {
        InventoryService inventoryService = new InventoryService();
        PaymentService paymentService = new RazorpayPaymentService();
        NotificationService notificationService = new NotificationService();

        OrderService orderService = new OrderService(inventoryService, paymentService, notificationService);
        
        Order order = new Order();
        orderService.checkout(order);

        //let's say we want to wrute tests for OrderService, we can easily mock the dependencies and test the checkout method in isolation.

        OrderService orderServiceForTest = new OrderService(new MockInventoryService(), new MockPaymentService(), new MockNotificationService());
    }
}
```

## Advantages of Dependency Injection


1. **Swappable Components**: Since the dependencies are injected, we can easily swap out one implementation for another without modifying the `OrderService` class. This adheres to the Open/Closed Principle (OCP) of SOLID principles.

2. **Testable with mocks**: With DI, we can easily inject mock implementations of the dependencies for testing purposes. This allows us to test the `OrderService` class in isolation without relying on external systems.

3. **Follows Dependency Inversion Principle (DIP)**: DI promotes the use of abstractions (interfaces) rather than concrete implementations, which aligns with the Dependency Inversion Principle of SOLID principles.


4. **Open to extension, Closed to modification**: With DI, we can extend the functionality of the `OrderService` class by injecting new dependencies without modifying its code. This makes the code more maintainable and less prone to bugs. Ex: new payment service can be added without changing the `OrderService` class.



## Types of Dependency Injection


### Constructor Injection

- In constructor injection, the dependencies are provided through the class constructor. 

- These dependencies are immutable, test-friendly and are available throughout the lifetime of the object.

- Constructor injection enforces required dependencies.


## Setter Injection

- In setter injection, the dependencies are provided through setter methods after the object is constructed.

- These dependencies are mutable and can be changed at runtime.

- This method can be misused by not calling the setter methods, leading to null pointer exceptions. It is not recommended to use setter injection for required dependencies.


```java
class PaymentService{
    private Logger logger;

    public void setLogger(Logger logger){
        this.logger = logger;
    }
}
```

## Interface Injection

- In interface injection, the dependency provides an injector interface that will be implemented by the client class. The client class will then use this interface to inject the dependency.

- Rarely used, requires interface changes to every class that is implementing the interface, and is not recommended for most use cases.

```java
Interface InjectorLogger{
    void injectLogger(Logger logger);
}
```


## When Dependency Injection is not needed?

- Tiny classes with zero dependencies, like utility classes, do not require DI.
- Static utility classes, like `Math` or `Collections`, do not require DI.
- One off scripts or tools which are we sure that will only be used once and will not be maintained in the future, do not require DI.



## When to use Dependency Injection?

| Symptom | Fix with DI |
| --- | --- |
| Classes use `new` for internal collaborators | Constructor Injection |
| Cannot mock services in tests | Inject dependency via abstract base class |
| Adding a feature breaks old code | Use abstraction and inject |
| Too many `if` / `switch` statements for service type | Inject strategy implementation |



----

check DI.java for technical implementation of Dependency Injection in Java.