package FoodDelivery.User;

public class User {
    private String name;

    public User(String name) {
        this.name = name;
        System.out.println("New user " + name + " logged in!");
    }

    public String getName() {
        return name;
    }
}