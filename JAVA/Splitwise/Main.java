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

        // check if all users in split exist in the group
        for(Map.Entry<User, Integer> s : split.entrySet()){
            String name = s.getKey().getName();
            if(!this.users.contains(s.getKey())) throw new Exception(name + " is not in the group!");
        }

        SplitStrategy newStrategy = StrategyFactory.getInstance(strategy);

        HashMap<User, Integer> splitValues = newStrategy.getSplitValues(amount, split);

        if(splitValues.isEmpty()) return;

        Expense newExpense = new Expense(user, title, amount, newStrategy, splitValues);

        this.expenses.add(newExpense);

        this.updateSheet(user, splitValues);
    }

    public void printSheet() {
        System.out.print("\t");
        for(User u : this.users){
            System.out.print(u.getName() + "\t");
        }

        System.out.println();

        for (Map.Entry<User, HashMap<User, Integer>> rowEntry : this.sheet.entrySet()) {
            User rowUser = rowEntry.getKey();
            Map<User, Integer> cols = rowEntry.getValue();

            System.out.print(rowUser.getName() + "\t");

            for(User u : this.users){
                Integer amt = cols.getOrDefault(u, 0);
                if(u.equals(rowUser)) System.out.print("-" + "\t");
                else System.out.print(amt + "\t");
            }
            System.out.println();
        }
    }

    private void updateSheet(User userAddingSplit, HashMap<User, Integer> split) {
        HashMap<User, Integer> userAddingBalanceSheet = this.sheet.getOrDefault(userAddingSplit, new HashMap<>());

        for (Map.Entry<User, Integer> s : split.entrySet()) {
            User userGettingSplit = s.getKey();
            if (userGettingSplit.equals(userAddingSplit)) {
                continue;
            }

            Integer amountToAdd = s.getValue();

            // What userAddingSplit thinks userGettingSplit owes
            Integer existingForward = userAddingBalanceSheet.getOrDefault(userGettingSplit, 0);

            // What userGettingSplit thinks userAddingSplit owes
            HashMap<User, Integer> reverseSheet =
                    this.sheet.getOrDefault(userGettingSplit, new HashMap<>());
            Integer existingReverse = reverseSheet.getOrDefault(userAddingSplit, 0);

            /**
             * CASE 1:
             * reverseSheet has a positive entry → userGettingSplit owes userAddingSplit.
             * We need to cancel out amounts.
             */
            if (existingReverse > 0) {

                if (existingReverse >= amountToAdd) {
                    // Reverse fully covers forward
                    reverseSheet.put(userAddingSplit, existingReverse - amountToAdd);
                    this.sheet.put(userGettingSplit, reverseSheet);
                    continue; // done for this user
                } else {
                    // Reverse only partially covers, cancel & reduce remaining
                    amountToAdd -= existingReverse;
                    reverseSheet.put(userAddingSplit, 0);
                    this.sheet.put(userGettingSplit, reverseSheet);
                }
            }

            /**
             * CASE 2:
             * Add the remaining amount to existing forward sheet.
             */
            userAddingBalanceSheet.put(userGettingSplit, existingForward + amountToAdd);
        }

        this.sheet.put(userAddingSplit, userAddingBalanceSheet);
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
    public abstract HashMap<User, Integer> getSplitValues(Integer amount, HashMap<User, Integer> split) throws Exception;
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
    public HashMap<User, Integer> getSplitValues(Integer amount, HashMap<User, Integer> split) throws Exception {
        Integer initialValue = null;
        Integer sum = 0;
        for (Map.Entry<User, Integer> s : split.entrySet()) {
            if (initialValue == null)
                initialValue = s.getValue();
            else {
                if (!s.getValue().equals(initialValue))
                    throw new Exception("All splits must be equal to procced");
            }
            sum += s.getValue();
        }

        if (amount.equals(sum))
            return split;

        throw new Exception("The sum of all splits much exactly match the amount");
    }
};

class ExactStrategy extends SplitStrategy {
    @Override
    public HashMap<User, Integer> getSplitValues(Integer amount, HashMap<User, Integer> split) throws Exception {
        Integer sum = 0;
        for (Map.Entry<User, Integer> s : split.entrySet()) {
            sum += s.getValue();
        }

        if (amount.equals(sum))
            return split;

        throw new Exception("The sum of all splits much exactly match the amount");
    }
};

class PercentStrategy extends SplitStrategy {
    @Override
    public HashMap<User, Integer> getSplitValues(Integer amount, HashMap<User, Integer> split) throws Exception {
        Integer totalPercentage = 0;
        HashMap<User, Integer> splitValues = new HashMap<>();
        for (Map.Entry<User, Integer> s : split.entrySet()) {
            User key = s.getKey();
            Integer percentage = s.getValue();

            if (percentage < 0) {
                throw new Exception("Percentage cannot be negative");
            }

            totalPercentage += percentage;

            Integer splitAmount = (percentage * amount) / 100;
            splitValues.put(key, splitAmount);
        }

        if (totalPercentage.equals(100))
            return splitValues;

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
        System.out.println("--------------");


            HashMap<User, Integer> split2 = new HashMap<>();
            split2.put(karan, 450);
            g1.addExpense(rahul, "Food", 450, "Equal", split2);

                        g1.printSheet();

        System.out.println("--------------");

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