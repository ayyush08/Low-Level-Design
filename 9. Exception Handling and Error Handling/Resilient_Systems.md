# Building Resilient Systems

## Error Handling

Error handling is the process of anticipating, detecting, and resolving issues during system execution. 

It ensures that our system responds to  failures gracefully without corrupting data or collapsing user experience. Effective error handling involves:

## Resilience

It is the system's ability to absorb failures and continue operating.

Robust systems recover from failures whereas Brittle systems crash or become unresponsive.

Resilience is not about avoiding failures but surviving them with minimal disruption to services.

## Robust vs Brittle Systems

| Characteristic | Robust System | Brittle System |
|---|---|---|
| System Behavior | Continues on demand gracefully. | Crashes or freezes. |
| Error Handling | Shows cached content or provides degraded service. | Entire system halts on error. |
| Example | Netflix showing cached content when the service is down. | Amazon checkout page crashes if payment service is down. |
| User Experience | Minimizes disruption to the user experience. | User experience is severely disrupted. |
| Recovery from Failures | Can recover or continue with limited functionality. | Fails completely with no fallback. |
| Example Scenario | Amazon checkout page hides recommendation services when the checkout fails. | Amazon checkout fails when payment service is down. |
---

<br/>

**Real-World Example:**

Imagine you're shopping on Amazon. When you try to purchase an item, the Amazon checkout page is a critical part of the transaction. If this page is part of a brittle system, and the payment service goes down, the entire checkout process may fail, causing the user to abandon the purchase and leading to a negative experience.

However, if Amazon's checkout page is designed as a robust system, even if the payment service fails, the system could still allow the user to complete the purchase by hiding the recommendation service or by offering the ability to retry. This ensures that the user can still proceed without facing a complete breakdown, demonstrating how a robust system maintains functionality under partial failure.

## Graceful Degradation Strategies

There are three main strategies for graceful degradation:

- **Return Cached Data:** When a service fails, return previously cached data to maintain functionality.

- **Show Fallback UI**: Provide a simplified user interface that allows users to continue interacting with the system, even if some features are unavailable.

- **Queue Requests**: Instead of failing immediately, queue requests for later processing when the service becomes available again.

```java

//Returning cached data example
class RecommendationService {
    public List<String> getRecommendedItems(String userId) {
        try {
            // Attempt to fetch live recommendations
            return recommendationService.fetchLiveRecommendations(userId);
        } catch (Exception ex) {
            // If the live service fails, log the error and fall back to cache
            log.warn("Live service failed, falling back to cache");
            return cacheService.getCachedRecommendations(userId);  // Fallback to cached data
        }
    }

    public List<String> fetchLiveRecommendations(String userId) {
        return List.of("movie-1", "movie-2");  // Simulated live recommendation data
    }
}

// Show fallback UI
public Menu getMenu(String restaurantId) {
    try {
        // Attempt to fetch the live menu
        return menuService.fetchMenu(restaurantId);
    } catch (Exception e) {
        // If the live menu is unavailable, show a fallback message in the UI
        return new Menu("Menu currently unavailable. Please try again later.");
    }
}


// Queue request's
public void placeOrder(Order order) {
    try {
        // Attempt to charge the payment
        paymentService.charge(order);
    } catch (Exception e) {
        // If payment fails, queue the order for retry
        orderRetryQueue.enqueue(order);
        log.warn("Payment failed. Queued for retry.");
    }
}
```

## Retry Mechanisms

There are two main types of retry mechanisms:

- **Naive Retry**: This approach retries the operation a fixed number of times without any delay or backoff strategy. It is simple but can lead to overwhelming the system if the failure is persistent.

- **Backoff Strategy**: This approach introduces a delay between retries, often increasing the delay with each subsequent attempt. This helps to reduce the load on the system and increases the chances of success in case of transient failures.


```java
//Naive Retry Example
public String getETA(){
    int retries = 3;
    while(retries > 0){
        try{
            return etaService.fetchETA();
        } catch (Exception e){
            retries--;
            log.warn("Failed to fetch ETA. Retries left: " + retries);
        }
    }
}

//Backoff Strategy Example
public String getETAWithBackoff() throws InterruptedException {
    int retries = 3;
    int backoffTime = 1000; // Initial backoff time in milliseconds
    while(retries > 0){
        try{
            return etaService.fetchETA();
        } catch (Exception e){
            retries--;
            log.warn("Failed to fetch ETA. Retries left: " + retries);
            if(retries > 0){
                try {
                    Thread.sleep(backoffTime); // Wait before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                }
                backoffTime *= 2; // Exponential backoff
            }
        }
    }
    return "ETA unavailable";
}
```

## Circuit Breaker Pattern

**Problem:** When a service is down, repeated attempts to call it can lead to cascading failures across the system.

**Solution:** Stop calling the failing service after a certain number of failures , wait and  test again later. This prevents the system from being overwhelmed and allows it to recover.

**States of Circuit Breaker:**

    - **CLOSED:** Normal operation, requests are allowed to pass through.
    - **OPEN:** The circuit is open, requests are blocked to prevent further failures.
    - **HALF-OPEN:** After a timeout, the circuit allows a limited number of test requests to check if the service has recovered.

```java
@Service
class PaymentService {

    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    public String charge(String userId, double amount) {
        // real payment logic
        return externalPaymentApi.charge(userId, amount);
    }

    // Fallback method in case of failure
    public String paymentFallback(String userId, double amount, Throwable t) {
        log.error("Payment Service Down. Fallback triggered.");
        return "PAYMENT_FAILED";
    }
}
```
**JAVA CONFIG CUSTOMIZATION:**
```java
@Bean
public Customizer<CircuitBreakerConfigCustomizer> paymentCircuitBreakerConfig() {
    return CircuitBreakerConfigCustomizer.of("paymentService", builder -> builder
        .slidingWindowSize(10)
        .failureRateThreshold(50)
        .waitDurationInOpenState(Duration.ofSeconds(10))
        .permittedNumberOfCallsInHalfOpenState(2)
        .automaticTransitionFromOpenToHalfOpenEnabled(true));
}
```

## Failover Strategy


Failover is the process of switching to a standby or alternative service when the primary one is unavailable or down. This is a proactive resilience strategy that ensures high availability and continued service delivery even when components fail.

Example : Switching to Stripe Payment Gateway when PayPal is down.



## Timeout Strategy

A timeout is a mechanism used to prevent long waits or hangs when a service becomes unresponsive. Instead of waiting indefinitely for a response, a timeout defines a maximum amount of time to wait before the request is aborted.

Example: If a payment service does not respond within 5 seconds, the system should abort the request and return an error message to the user, allowing them to retry or choose an alternative payment method.
