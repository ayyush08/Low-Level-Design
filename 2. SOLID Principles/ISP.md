# Interface Segregation Principle (ISP)

ISP states that no client should be forced to depend on methods it does not use. 

Don't create large, bloated interface. Break them into smaller and more specific interfaces so that clients will only have to know about the methods that are of interest to them.

## Example (Uber):

### Bad Design (Violating ISP)

```java
interface Uber{
    void bookCab();
    void acceptRide();
    void drive();
    void endRide();
    void payRide();
}

class Rider implements Uber{
    @Override
    public void bookCab() {
        System.out.println("Booking a cab...");
    }

    @Override
    public void acceptRide() {
        // Rider does not need to accept ride, but forced to implement it
        throw new UnsupportedOperationException("Rider cannot accept ride");
    }

    @Override
    public void drive() {
        // Rider does not need to drive, but forced to implement it
        throw new UnsupportedOperationException("Rider cannot drive");
    }

    @Override
    public void endRide() {
        // Rider does not need to end ride, but forced to implement it
        throw new UnsupportedOperationException("Rider cannot end ride");
    }

    @Override
    public void payRide() {
        System.out.println("Paying for the ride...");
    }
}

class Driver implements Uber{
    @Override
    public void bookCab() {
        // Driver does not need to book cab, but forced to implement it
        throw new UnsupportedOperationException("Driver cannot book cab");
    }

    @Override
    public void acceptRide() {
        System.out.println("Accepting the ride...");
    }

    @Override
    public void drive() {
        System.out.println("Driving the cab...");
    }

    @Override
    public void endRide() {
        System.out.println("Ending the ride...");
    }

    @Override
    public void payRide() {
        // Driver does not need to pay for the ride, but forced to implement it
        throw new UnsupportedOperationException("Driver cannot pay for the ride");
    }
}
```

### Good Design (Adhering to ISP)

```java
interface RiderInterface {
    void bookCab();
    void payRide();
}

interface DriverInterface {
    void acceptRide();
    void drive();
    void endRide();
}

class Rider implements RiderInterface{
    @Override
    public void bookCab() {
        System.out.println("Booking a cab...");
    }

    @Override
    public void payRide() {
        System.out.println("Paying for the ride...");
    }
}

class Driver implements DriverInterface{
    @Override
    public void acceptRide() {
        System.out.println("Accepting the ride...");
    }

    @Override
    public void drive() {
        System.out.println("Driving the cab...");
    }

    @Override
    public void endRide() {
        System.out.println("Ending the ride...");
    }
}
```

As we can see in the above example, we have broken down the large `Uber` interface into two smaller interfaces: `RiderInterface` and `DriverInterface`. This way, the `Rider` class only implements the methods it needs, and the `Driver` class only implements the methods it needs. This adheres to the Interface Segregation Principle (ISP).


## Benefits of ISP

- **Modularity & Flexibility**: By breaking down interfaces into smaller, more specific ones, we can create more modular and flexible code. This allows for easier maintenance and updates to the system.

- **Improved Testability**: Smaller interfaces make it easier to write unit tests for individual components, leading to improved testability and better code quality.

- **Prevents accidental implementation**: By forcing classes to implement only the methods they need, we prevent accidental implementation of methods that are not relevant to their functionality. (Rider cannot implement `acceptRide()` method, and Driver cannot implement `bookCab()` method)

- **Easier to understand**: Smaller interfaces are easier to understand and use, making it easier for developers to work with the codebase.


## When to apply ISP?

1. When your interface is doing too many things.
2. When some classes implementing an interface throw some errors (Rider implementing `acceptRide()` method, and Driver implementing `bookCab()` method) because they don't need to implement those methods.