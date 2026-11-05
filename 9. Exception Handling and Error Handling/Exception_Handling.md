# Exception Handling


## Problem : Payment Gateway in Amazon

Imagine you're building a payment gateway for Amazon. As a developer, you always focus on designing the "happy path", which refers to the expected sequence of events that occurs during a successful checkout like adding items to the cart,  cart accumulation, and payment processing,etc.

**IMPACT of Poor Exception Handling**

However, in real-world scenarios, things don't always go as planned. There are various exceptions that can occur during the payment process, such as:

1. **Failed Transaction**: The payment gateway may fail to process the payment due to insufficient funds, network issues, or other technical problems.

2. **Angry Customer**: A customer may become frustrated if their payment fails, leading to negative reviews or complaints.

3. **Cart Abandonment** : A user when faced with a failed transaction or OTP failures may abandon their cart and leave the website, resulting in lost sales.

4. **Bad Public Relations(PR): A customer facing recurring issues may post on social media or review platforms, leading to negative publicity for the company.

**GOOD Exception Handling Practices**

1. **Proper Error Messages**: Provide clear and concise error messages to the user, explaining what went wrong and how they can resolve the issue.

2. **Logs of Root Cause**: Maintain detailed logs of exceptions and errors to help identify the root cause of the problem and facilitate debugging.

3. **Retry Mechanism**: Implement a retry mechanism for failed transactions, allowing users to attempt the payment again without losing their cart items.

4. **Graceful Failures**: Design the system to handle failures gracefully, ensuring that users can continue their shopping experience without significant disruption.


## Fail-fast vs Fail-safe


| Approach | Description | Example | Best suited for |
|---|---|---|---|
| **Fail-fast** | Detect and stop at the first sign of failure. | `NullPointerException` on missing user profiles. | Data-integrity-critical systems, to avoid cascading failures. |
| **Fail-safe** | Try to recover and continue working if possible. | Guest account on the Apple website allows users to continue shopping without a registered account. | User-experience-centric flows, to ensure a smooth experience despite errors. |


```java
class ProductServiceFailSafe{
    public Product getProduct(String productId){
        try{
            return productRepository.findById(productId);
        }
        catch(Exceptino e){
            return new Product("Default","Fallback Product",0.0);
        }
    }
}

class ProductServiceFailFast{
    public Product getProduct(String productId){
        if(productId==null){
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        return productRepository.findById(productId);
    }
}
```

## Checked Exceptions


Checked exceptions are exceptions that are checked at compile-time. Java forces you to either handle them using a try-catch block or declare them in the method signature using the `throws` keyword. 

These are used when 
    - The exception is recoverable .
    - It is part of the business logic.

Example: `IOException`, `SQLException`, `FileNotFoundException`

```java
//Using throws keyword to declare checked exception
class Logging1{
    public void readFile(String filePath) throws IOException { //checked exception
        BufferedReader reader = new BufferedReader(new FileReader(filePath)); //can throw FileNotFoundException
    }
}


//Using try-catch block to handle checked exception
class FileProcessor{
    public void readFile(String filePath){
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
        }
        catch(IOException e){ //checked exception
            System.out.println("Error reading file: " + e.getMessage());
    }
}
}
```

**Use when**: 
    - The client can do something to recover from the exception.
    - It is an expected failure from the external system, like a network failure or a missing file.


## Unchecked Exceptions

Unchecked exceptions are exceptions that are not checked at compile-time. They are subclasses of `RuntimeException` and do not require explicit handling.

**Used when**: 
    - It's a programming bug, like null pointer or illegal argument.
    - It's not expected to be handled every time

Example: `NullPointerException`, `ArrayIndexOutOfBoundsException`, `IllegalArgumentException`



## Custom Exceptions

They are user-defined exceptions tailored to your application's specific needs. Instead of relying solely on generic exceptions, you define something more meaningful that conveys the nature of the error in your domain.

Why? - To write expressive, maintainable and debuggable code that aligns with your business logic.


**When to use**:


1. Want to represent a specific domain's layer (UserNotFoundException, PaymentFailedException).
2. Wrap lower-level exceptions to provide high-level errors to users (e.g., wrapping a `SQLException` in a `DataAccessException`).
3. Want to provide clear separation of business logic and error handling, making it easier to understand and maintain the codebase.
4. Building an API and wanting structural responses.

```java

class CustomerNotPlusException extends RuntimeException{
        public CustomerNotPlusException(String userId){
            super("Customer with ID " + userId + " is not a Plus member.");
        }
}

class CourseService{
    public void accessCourse(String userId){
        if(!hasAccess(userId)){
            throw new CustomerNotPlusException(userId);
        }
        //continue enrollment
    }
    private boolean hasAccess(String userId){
        //logic to check if user is a plus member
        return false;
    }
}
```