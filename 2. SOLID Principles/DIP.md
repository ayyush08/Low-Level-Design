# Dependency Inversion Principle (DIP)

DIP says that High-level modules should not depend on low-level modules. 
Both should depend on abstractions (e.g., interfaces).
Abstrations should not depend on details.
Details should depend on abstractions.


## Real Life Example (Recommendation Algorithms)


### Bad Design (Violating DIP)

```java
class TrendingRecommendation {
    public void recommend() {
        System.out.println("Trending Recommendation");
    }
}
class GenreRecommendation {
    public void recommend() {
        System.out.println("Genre Recommendation");
    }
}

class RecentRecommendation {
    public void recentRecommend() {
        System.out.println("Recent Recommendation");
    }
}

class RecommendationService{
    public static void main(String[] args) {
        TrendingRecommendation trendingRecommendation = new TrendingRecommendation();
        trendingRecommendation.recommend();

        GenreRecommendation genreRecommendation = new GenreRecommendation();
        genreRecommendation.recommend();

        RecentRecommendation recentRecommendation = new RecentRecommendation();
        recentRecommendation.recentRecommend(); //Different method name, not adhering to a common interface
    }
}
```

We can see each recommendation class is tightly coupled with the `RecommendationService` class. If we want to add a new recommendation type, we would have to modify the `RecommendationService` class, which violates the DIP.


### Good Design (Adhering to DIP)

```java
interface RecommendationStrategy {
    void recommend();
}

class TrendingRecommendation implements RecommendationStrategy {
    public void recommend() {
        System.out.println("Trending Recommendation");
    }
}

class GenreRecommendation implements RecommendationStrategy {
    public void recommend() {
        System.out.println("Genre Recommendation");
    }
}

class RecentRecommendation implements RecommendationStrategy {
    public void recommend() {
        System.out.println("Recent Recommendation");
    }
}

class RecommendationService {
    private RecommendationStrategy strategy;

    RecommendationService(RecommendationStrategy strategy) {
        this.strategy = strategy;
    }

    public void recommend() {
        strategy.recommend();
    }
}
class Main {
    public static void main(String[] args) {
        RecommendationService service = new RecommendationService(new TrendingRecommendation()); // You can easily switch to a different recommendation strategy without modifying the RecommendationService class
        service.recommend();
    }
}
```
Here, the `RecommendationService` class depends on the `RecommendationStrategy` interface rather than the concrete recommendation classes. This allows for easy addition of new recommendation types without modifying the existing code, adhering to the Dependency Inversion Principle.

In the above example, the high-level module (RecommendationService) and the low-level modules (TrendingRecommendation, GenreRecommendation, RecentRecommendation) both depend on the abstraction (RecommendationStrategy). The abstraction does not depend on the concrete implementation details; instead, the concrete recommendation classes depend on the abstraction by implementing the interface. This means abstractions should not depend on details; details should depend on abstractions, which is the essence of the Dependency Inversion Principle.


## Benefits of DIP

- **Loosely coupled to interfaces**: By depending on abstractions rather than concrete implementations, the system becomes more flexible and easier to maintain.

- **Just add new implementations**: New implementations can be added without modifying existing code, making the system more extensible.

- **Easier to test**: By depending on interfaces, it becomes easier to mock dependencies and write unit tests for the high-level modules.

- **Abstraction controls flow**: The high-level module controls the flow of the application, while the low-level modules provide the implementation details. This separation of concerns leads to a more maintainable and scalable system.

- **Fully open/closed friendly**: The system is fully open for extension and closed for modification, allowing for easy addition of new features without breaking existing functionality.