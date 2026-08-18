# Open Closed Principle (OCP)

Software entities (classes, modules, functions, etc.) should be open for extension but closed for modification.

You should be able to add new behaviour to a class or module  without modifying its existing code. 


## Real Life Analogy (Adapter)

Imagine you travel from India to the UK. Your Indian charger doesn't fit into UK power sockets. Instead of buying a new charger, you use a travel adapter.

- The adapter extends your existing charger's usability (now works in UK).
- You did not modify the charger itself.

Similarly, in code, OCP encourages adding new functionality via extension, rather than altering existing, stable code.


## Real Life Example (Tax Calculation)

Suppose, you have a tax calculation system that calculates tax for different countries. Initially, you have a class `TaxCalculator` that calculates tax for India.


### Bad Design (Violating OCP)

```java
class TaxCalculator {
    public double calculateTax(double amount) {
        // Tax calculation logic for India
        return amount * 0.18; // 18% GST
    }
}

class TaxService {

    public double getTax(double amount) {
        TaxCalculator taxCalculator = new TaxCalculator();
        return taxCalculator.calculateTax(amount);
    }
}
```

Now, if you want to add tax calculation for the UK, you would have to modify the `TaxCalculator` class, which violates the OCP.

### Good Design (Adhering to OCP)

```java
interface TaxCalculator {
    double calculateTax(double amount);
}
class IndianTax implements TaxCalculator {
    public double calculateTax(double amount) {
        return amount * 0.18; // 18% GST
    }
}

class UKTax implements TaxCalculator {
    public double calculateTax(double amount) {
        return amount * 0.20; // 20% VAT
    }
}

class TaxService {

    public void getTax(double amount) {
        TaxCalculator indianTaxCalculator = new IndianTax();
        System.out.println("Calculating tax for India:"+ indianTaxCalculator.calculateTax(amount));

        TaxCalculator ukTaxCalculator = new UKTax();
        System.out.println("Calculating tax for UK:"+ ukTaxCalculator.calculateTax(amount));

    }
}
```

In this design, if you want to add tax calculation for another country, you can simply create a new class that implements the `TaxCalculator` interface without modifying the existing classes. This adheres to the Open Closed Principle.


## When to apply OCP?

- When you have a business rule that is likely to change or expand in the future.

- When you're building a plugin system (e.g., using AWS today but if you want to switch to Azure tomorrow, then Azure is supposed to be written in such a way that it can be plugged in without changing the existing code or atleast doing minimal changes).

- When your codebase is becoming a  **God Class* with a lot of conditionals. 

## Misconcenptions about OCP

| Misconception | Reality |
|---|---|
| OCP means never touching old | No, you can refactor old code to support OCP |
| OCP leads to more classes so it's an overkill | Extra classes are fine if they improve maintainability |
| It makes code harder to read | Not if done right, you instead gain a lot of flexibility |

