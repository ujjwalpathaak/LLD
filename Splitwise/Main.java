package Splitwise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class User {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
};

class Group {
    private String name;
    private ArrayList<User> users = new ArrayList<User>();
    private ArrayList<Expense> expenses = new ArrayList<Expense>();
    private HashMap<User, HashMap<User, Integer>> sheet = new HashMap<>();

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Boolean findUser(User user) {
        return users.contains(user);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addExpense(User user, String title, Integer amount, String strategy,
            HashMap<User, Integer> split) throws Exception {

        SplitStrategy newStrategy = StrategyFactory.getInstance(strategy);
        
        if(!newStrategy.isSplitValid(amount, split)) return;

        Expense newExpense = new Expense(user, title, amount, newStrategy, split);

        this.expenses.add(newExpense);

        this.updateSheet(user, amount, split);
    }

    public void printSheet() {
                System.out.println("--------------");

        for (Map.Entry<User, HashMap<User, Integer>> rowEntry : this.sheet.entrySet()) {
            User rowUser = rowEntry.getKey();
            Map<User, Integer> cols = rowEntry.getValue();

            System.out.print("\t");

            for (Map.Entry<User, Integer> col : cols.entrySet()) {
                User user = col.getKey();
                System.out.print(user.getName() + "\t");
            }

            System.out.println();
            System.out.print(rowUser.getName() + "\t");

            for (Map.Entry<User, Integer> col : cols.entrySet()) {
                Integer val = col.getValue();
                System.out.print(val + "\t");
            }

            System.out.println();
        }
    }

    private void updateSheet(User user, Integer amount, HashMap<User, Integer> split) {
        HashMap<User, Integer> col = this.sheet.getOrDefault(user, new HashMap<>());

        for (Map.Entry<User, Integer> s : split.entrySet()) {
            User u = s.getKey();
            if (u.equals(user)) {
                continue;
            }
            Integer amt = s.getValue();
            Integer pendingAmt = col.getOrDefault(u, 0);

            HashMap<User, Integer> userBalance = this.getUserBalance(u);
            Integer pendingBalance = userBalance.getOrDefault(user, 0);

            if (pendingBalance > 0) {
                if (pendingBalance >= amt) {
                    pendingBalance -= amt;
                    userBalance.put(user, pendingBalance);
                    this.sheet.put(u, userBalance);
                    continue;
                } else {
                    amt -= pendingBalance;
                    col.put(u, amt);
                }
            } else {
                amt += pendingAmt;
                col.put(u, amt);
            }
        }
        this.sheet.put(user, col);
    }

    public HashMap<User, Integer> getUserBalance(User user) {
        return this.sheet.getOrDefault(user, new HashMap<>());
    }
};

class Expense {
    private User user;
    private Integer amount;
    private SplitStrategy strategy;
    private String title;
    private HashMap<User, Integer> split;

    public Expense(User user, String title, Integer amount, SplitStrategy strategy, HashMap<User, Integer> split) {
        this.title = title.isEmpty() ? "Splitwise Group" : title;
        this.user = user;
        this.amount = amount;
        this.strategy = strategy;
        this.split = split;
    };
};

abstract class SplitStrategy {
    public abstract Boolean isSplitValid(Integer amount, HashMap<User, Integer> split) throws Exception;
};

class StrategyFactory {
    public static SplitStrategy getInstance(String strategy) throws Exception {
        switch (strategy) {
            case "Equal":
                return new EqualStrategy();

            case "Exact":
                return new ExactStrategy();

            case "Percentage":
                return new PercentStrategy();

            default:
                throw new Exception("Unknown strategy: " + strategy);
        }
    }
};

class EqualStrategy extends SplitStrategy {
    @Override
    public Boolean isSplitValid(Integer amount, HashMap<User, Integer> split) throws Exception {
        Integer initialValue = -1;
        for (Map.Entry<User, Integer> s : split.entrySet()) {
            if (initialValue.equals(-1))
                initialValue = s.getValue();
            else {
                if (!s.getValue().equals(initialValue))
                    throw new Exception("All splits must be equal to procced");
            }
            amount -= initialValue;
        }

        if (amount.equals(0))
            return true;

        throw new Exception("The sum of all splits much exactly match the amount");
    }
};

class ExactStrategy extends SplitStrategy {
    @Override
    public Boolean isSplitValid(Integer amount, HashMap<User, Integer> split) throws Exception {
        Integer initialValue = 0;
        for (Map.Entry<User, Integer> s : split.entrySet()) {
            initialValue += s.getValue();
        }

        if (amount.equals(initialValue))
            return true;

        throw new Exception("The sum of all splits much exactly match the amount");
    }
};

class PercentStrategy extends SplitStrategy {
    @Override
    public Boolean isSplitValid(Integer amount, HashMap<User, Integer> split) throws Exception {
        Integer totalPercentage = 0;
        for (Map.Entry<User, Integer> s : split.entrySet()) {
            User key = s.getKey();
            Integer percentage = s.getValue();
            totalPercentage += percentage;

            Integer splitAmount = (percentage * amount) / 100;
            split.put(key, splitAmount);
        }

        if (totalPercentage.equals(100))
            return true;

        throw new Exception("The total percentage should equal to 100");
    }
};

public class Main {
    public static void main(String args[]) {
        try {
            User rahul = new User("Rahul");
            User karan = new User("Karan");
            User praful = new User("Praful");
            User u4 = new User("Lokesh");
            User u5 = new User("Santosh");

            Group g1 = new Group("Goa Trip");
            Group g2 = new Group("Dinner Party");

            g1.addUser(rahul);
            g1.addUser(karan);
            g1.addUser(praful);

            HashMap<User, Integer> split1 = new HashMap<>();
            split1.put(rahul, 30);
            split1.put(praful, 70);
            g1.addExpense(rahul, "Flight", 300, "Percentage", split1);

                        g1.printSheet();


            HashMap<User, Integer> split2 = new HashMap<>();
            split2.put(karan, 450);
            g1.addExpense(rahul, "Food", 450, "Equal", split2);

                        g1.printSheet();


            HashMap<User, Integer> split3 = new HashMap<>();
            split3.put(rahul, 80);
            split3.put(karan, 60);
            split3.put(praful, 70);
            g1.addExpense(karan, "Cab", 210, "Exact", split3);

            g1.printSheet();
        System.out.println("--------------");

        } catch (Exception err) {
            System.out.println("\n" + err.getMessage() + "\n");
        }
    }
}

// ❌ 2. EqualStrategy logic is wrong

// You wrote:

// amount -= initialValue;

// This subtracts same initialValue for every user, instead of:

// amount -= s.getValue()

// Also initialValue = -1 logic is hacky.

// This makes split validation unreliable → interviewer will reject.

// ⸻

// ❌ 3. PercentStrategy modifies the caller’s split map

// This is a serious bug:

// split.put(key, splitAmount);

// You mutate the user input map → unexpected side effects.

// Example:

// split = {Rahul=30, Praful=70}   <-- percentage
// you convert it to amounts {Rahul=90, Praful=210}

// But the caller still believes it’s percentage.
// Very dangerous behavior.

// ⸻

// ❌ 4. updateSheet logic is extremely complex

// Your balancing logic:

// if (pendingBalance > 0) { ... }
// else { ... }

// Problems:
// 	•	Hard to understand
// 	•	Not mathematically correct for all cases
// 	•	Does not simplify 3-way cycles
// 	•	Fails when multiple debts exist
// 	•	You’re trying to manually reconcile A owes B and B owes A → but doing it incorrectly

// Tier-1 companies expect a simple, correct balancing model, like:

// balance[A] += paid[A] - owed[A]

// Then a settling algorithm.

// Your approach will break under multiple transactions.

// ⸻

// ❌ 5. printSheet() is wrong

// You print columns based on row entry, not global users.

// So if one user owes only 2 users, sheet prints only those 2 columns → inconsistent table.

// Tier-1 expects deterministic, stable ordering:

// Rahul   Karan   Praful
// ...


// ⸻

// ❌ 6. Group allows addExpense with users not in group

// No validation:

// if(!users.contains(user)) throw ...

// Tier-1 companies expect strict validation.

// ⸻

// ❌ 7. Group.findUser() isn’t used properly

// findUser() is redundant since you never use it.

// ⸻

// ❌ 8. Expense class has unused fields

// strategy is stored but never used anywhere afterward.