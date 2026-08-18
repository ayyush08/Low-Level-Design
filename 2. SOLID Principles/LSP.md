# Liskov Substitution Principle (LSP)

LSP states that if S is a subtype of T, then objects of type T may be replaced with objects of type S without altering any of the desirable properties of that program (correctness, task performed, etc.).

**Simpler Terms:**

If class B is a subclass of class A, then we should be able to use B anywhere we used A and the behaviour of the program should remain correct.


**Example:**

```java

class Rectangle {
    protected int width;
    protected int height;

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getArea() {
        return width * height;
    }
}

class Square extends Rectangle {
    @Override
    public void setWidth(int width) {
        this.width = width;
        this.height = width; // Ensuring height is always equal to width
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
        this.width = height; // Ensuring width is always equal to height
    }
}


class AreaCalculator {
    public void printArea(){
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(5);
        rectangle.setHeight(10);
        System.out.println("Area of Rectangle: " + rectangle.getArea());

        Rectangle square = new Square();
        square.setWidth(5);
        System.out.println("Area of Square: " + square.getArea());
    }
}
``` 

## Classic Example (Notifications)

If you have a class `Notification` that sends notifications via email and in future you introduce a new notification type, say SMS, you should be able to use the new notification type without changing the existing code that uses the `Notification` class.

```java
class Notification {
    public void sendEmail(String message) {
        // Logic to send email
    }
}

class SMSNotification extends Notification {
    @Override
    public void sendSMS(String message) {
        // Logic to send SMS
    }
}

class WhatsAppNotification extends Notification {
    @Override
    public void sendWhatsApp(String message) {
        // Logic to send WhatsApp message
    }
}
```

## Why does LSP matter?

LSP violations leads to - 

 - Broken functionality when subclasses replace their parent classes.
 - Fragile inheritance hierarchies that are hard to maintain. (Be extremely careful when using inheritance for LSP)
 - Hard to detect bugs and unexpected behavior in the system.
 - Client code being tightly coupled to specific types, making it hard to extend or modify the system without breaking existing code.

## How to spot LSP violations?

To spot LSP violations, ask yourself these questions:
- Does the subclass override methods in a way that changes meaning or assumptions?
- Can I replace the base class with the subclass everywhere without changing expected behavior or breaking correctness?
- Does the subclass throw unexpected exceptions or return wrong values?
- Does the subclass weaken any preconditions or strengthen postconditions?

If the answer to any of these questions is "yes", there might be a LSP violation in the code.

## Key Principles to follow to adhere to LSP


1. **Design by Contract**: Subclasses should honor the contract (expectations) of the parent class. This means that they should not violate the preconditions, postconditions, or invariants established by the parent class.

2. **Avoid Overriding Methods that Break Behavior**: Subclasses should not override methods in a way that changes the expected behavior of the parent class. If a method in the parent class has certain expectations, the subclass should not violate those expectations.

3. **Use Composition over Inheritance**: Favor composition over inheritance when possible. This allows for more flexible designs and reduces the risk of LSP violations.

4. **Think in Terms of Interfaces**: Design your classes to depend on interfaces rather than concrete implementations. This allows for more flexibility and easier substitution of different implementations.

5. **Refactor Early**: If you find that a certain subclass is going to violate LSP in the future, consider refactoring your design early to avoid potential issues. This may involve creating new interfaces or abstract classes to better represent the relationships between classes.