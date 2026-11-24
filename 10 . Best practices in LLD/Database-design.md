# Database Design and Integration in LLD

## What is an ER Model?

An entity-relationship (ER) model represents entities, their attributes, and the relationships among them. It is the foundation for designing you database schema. 

**Why it matters in LLD?**:

    - Helps convert product requirements into data models.
    - Bridges real-world entities with database tables and object models.


## Problem Statement - Razorpay 

- A user initiates a payment to merchant.
- The payment might succeed or fail. (Success -> Transaction completed, Failure -> Transaction failed)
- Transaction may have refunds.
- Payment can be made via different methods (UPI, Card, Netbanking, Wallets, etc.)


**ER Model**:

| Entity | Attributes(Columns)
|---------|------------------|
| User | user_id(PK), name, email, phone_number |
| Merchant | merchant_id(PK), name, email, phone_number |
| Payment Request | payment_request_id(PK), user_id(FK), merchant_id(FK), amount, status, created_at |
| Transaction | transaction_id(PK), payment_request_id(FK), payment_method_id(FK), transaction_status, transaction_date |
| Refund | refund_id(PK), transaction_id(FK), refund_amount, refund_reason, refund_status, refund_date |
| Payment Method | payment_method_id(PK), method_name, method_type (UPI, Card, Netbanking, Wallets, etc.) |



## How to design tables from requirements?

1. **Nouns** = `Entities (Tables)`  (User,Merchant,Payment Method,etc)

2. **Properties** = `Attributes (Columns)` (name, email, phone_number, etc)

3. **Verbs/Actions** = `Relationships` (User initiates Payment Request, Payment Request has Transaction, Transaction may have Refund, etc)

4. **Cardinality** = `Relationship Type` (One-to-One, One-to-Many, Many-to-Many)




| Table | Key Relationships (Cardinality)
|-------|---------------------------|
| `User` | One user -> Many Payment Requests |
| `Merchant` | One merchant -> Many Payment Requests |
| `Payment Request` | Many requests -> One user, One Merchant|
| `Transaction` | Many transactions -> One Payment Request |
| `Refund` | Single refund -> One Transaction |
| `Payment Method` | Used in many transactions |



## Mapping ER to Class Model

Every table becomes a class, every column becomes a field and foreign keys become object references.

```java
class User {
    String userId;
    String name;
    String email;
    String phoneNumber;
    List<PaymentRequest> paymentRequests; // One-to-Many relationship
}

class Merchant {
    String merchantId;
    String name;
    List<PaymentRequest> paymentRequests; // One-to-Many relationship
}

class PaymentRequest {
    String paymentRequestId;
    User user; // One-to-One relationship
    Merchant merchant; // One-to-One relationship
    double amount;
    String status;
    Date createdAt;
    List<Transaction> transactions; // One-to-Many relationship
}

class Transaction {
    String transactionId;
    PaymentRequest paymentRequest; // One-to-One relationship
    PaymentMethod paymentMethod; // One-to-One relationship
    String transactionStatus;
    Date transactionDate;
    List<Refund> refunds; // One-to-Many relationship
}

class Refund{
    String refundId;
    Transaction transaction; // One-to-One relationship
    double refundAmount;
    String refundReason;
    String refundStatus;
    Date refundDate;
}

class PaymentMethod {
    String paymentMethodId;
    String methodName;
    String methodType; // UPI, Card, Netbanking, Wallets, etc.
    List<Transaction> transactions; // One-to-Many relationship
}
```


----

## What is DAO ( Data Access Object)?

A DAO is a class that provides  basic CRUD operations for a single entity. 

**Purpose of DAO**
- Encapsulates raw database logic.
- Keeps services clean from SQL/ORM clutter.
- Encourages separation of concerns and maintainability.

```java
class PaymentRequestDAO {
    save(PaymentRequest paymentRequest)
    findById(String paymentRequestId)
    updateStatus(String paymentRequestId, String newStatus)
}
```

- Does not support fetching related transactions or refunds. 
- Too low-level for business logic. 


---

## What is Repository?

A repository is a domain-centric abstraction that manages aggregates - not just on one table, but related data group logically.

**Purpose of Repository**
- Encapsulates complex joins and business rules.
- Provides high-level intent-based APIs (not just CRUD).

`Don't let services do SQL joins, Repositories should return fully populated models.`

```java
class PaymentRepository{
    createPayment(userId, merchantId, amount, currency)
    getFullPaymentDetails(paymentRequestId) // returns PaymentRequest with Transactions and Refunds
    getUserPaymentHistory(userId) // returns List<PaymentRequest> with Transactions and Refunds
    markPaymentAsFailed(paymentRequestId)
    refundTransaction(transactionId, refundAmount, refundReason) 
}

getFullPaymentDetails(paymentRequestId) {
    //Fetch PaymentRequest by ID
    //Fetch Transactions for the PaymentRequest
    //Fetch Refunds & PaymentMethod for each Transaction
    //return structured PaymentRequest object with all related data
}
```


## Real world enhancements and practices


| Concern | LLD Strategy
|---------|---------------------------|
| Duplicate payment | User `idempotency_key` in payment request to prevent duplicate processing. |
| Refunds must be atomic | Wrap refund+status in a `@Transactional` block to ensure atomicity. |
| Slow Dashboards | Use pagination |
| Query Explosions | Fetch relations eagerly or use joins  in repository |
| Scalling millions | Indexing (Individual or Composite) |


