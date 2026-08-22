# Builder Creational Pattern

Let us consider the example of a client ordering BurgerMeal 

```java
class BurgerMeal{
    private String patty;
    private String bun;

    public BurgerMeal(String patty, String bun,String sides, List<String> toppings){
        this.patty = patty;
        this.bun = bun;
    }
}
class Main{
    public static void main(String[] args) {
        BurgerMeal burgerMeal = new BurgerMeal("Chicken","Whole Wheat", null, null); // Client has to pass all the parameters even if they are not required
    }
}
```

In the above scenario, a patty and bun are something always required for a burger meal, but sides and toppings are optional. So, if the client wants to order a burger meal with only patty and bun, they still have to pass null for sides and toppings. Imagine the similar scenario but with 100s of options in the menu, it will be very difficult for the client to remember all the parameters and pass them in the correct order.

This is the problem that the Builder pattern solves. 

## Definition

It is a creational design pattern that lets you construct complex objects step by step. 

It separates the construction of an object from its representation. allowing the same construction process to create different representations.

- Problem : Burger Meal
        - choose Bun type
        - add patty
        - add toppings
        - add sides
        - add cheese, etc


```java
class BurgerMeal{
    //Required
    private final String patty;
    private final String bunType;

    //Optional
    private final boolean hasCheese;
    private final List<String> toppings;    
    private final String sides;
    private final String drink;

    //Private constructor to enforce the use of Builder
    private BurgerMeal(BurgerMealBuilder builder){
        this.patty = builder.patty;
        this.bunType = builder.bunType;
        this.hasCheese = builder.hasCheese;
        this.toppings = builder.toppings;
        this.sides = builder.sides;
        this.drink = builder.drink;
    }

    public static class BurgerMealBuilder{
        //Required
        private final String patty;
        private final String bunType;

        //Optional
        private boolean hasCheese;
        private List<String> toppings;
        private String sides;
        private String drink;

        public BurgerMealBuilder(String patty, String bunType){ //mandatory parameters
            this.patty = patty;
            this.bunType = bunType;
        }

        public BurgerMealBuilder withCheese(boolean hasCheese){
            this.hasCheese = hasCheese;
            return this;
        }

        public BurgerMealBuilder withToppings(List<String> toppings){
            this.toppings = toppings;
            return this;
        }

        public BurgerMealBuilder withSides(String sides){
            this.sides = sides;
            return this;
        }

        public BurgerMealBuilder withDrink(String drink){
            this.drink = drink;
            return this;
        }

        public BurgerMeal build(){
            return new BurgerMeal(this);
        }
    }
}

public class Main{
    public static void main(String[] args) {
        BurgerMeal burgerMeal = new BurgerMeal.BurgerMealBuilder("Chicken","Whole Wheat")
                .withCheese(true)
                .withToppings(Arrays.asList("Lettuce", "Tomato"))
                .withSides("Fries")
                // .withDrink("Coke") - customer said no drink
                .build();
    }
}
```

As we can see in the above example, the client can now create a BurgerMeal object with only the required parameters and can choose to add optional parameters as needed. The Builder pattern provides a clear and readable way to construct complex objects step by step.

## Telescoping Constructor Anti-Pattern (Not recommended to use at scale)

```java
class BurgerMeal{
    private String patty;
    private String bun;
    private boolean hasCheese;

    public BurgerMeal(String patty, String bun){
        this.patty = patty;
        this.bun = bun;
    }

    public BurgerMeal(String patty, String bun, boolean hasCheese){
        this.patty = patty;
        this.bun = bun;
        this.hasCheese = hasCheese;
    }
}
class Main{
    public static void main(String[] args) {
        BurgerMeal burgerMeal = new BurgerMeal("Chicken","Whole Wheat", true); 
        BurgerMeal burgerMealwithoutCheese = new BurgerMeal("Chicken","Whole Wheat");

    }
}
```

Above method seems like a hack to solve the problem of optional parameters, but it can lead to a lot of constructor overloads and can become difficult to manage as the number of optional parameters increases. This is known as the Telescoping Constructor Anti-Pattern and is not recommended for use at scale.

- Java lacks optional/default parameters, so we have to use workarounds like the Builder pattern to achieve similar functionality.

## When to use?

- When you have a complex object with many optional parameters.
- Immutability is desired for the constructed object.
- You want readable and maintainable code for object construction.

## When to avoid?

- Your class is simple with 1-2 fields and does not have many optional parameters.
- You do not need object customization or immutability.


## Pros 

- Avoids constructor telescoping
- ensures immutability of the constructed object
- cleaner, readable, and maintainable code for object construction
- Great for comlex configurations

## Cons
- Slightly tough to set up 
- overkill for simple objects with few fields
- separate builder class can lead to more code and complexity


## Real world products that use Builder pattern

- Lombok library in Java uses Builder pattern to generate builder classes for annotated classes.
- Cart from Amazon uses Builder pattern to create complex cart objects with various optional parameters.