package FoodDelivery.MenuItem;

public class MenuItem {
    String name;
    Number price;

    public MenuItem(String name, Number price){
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return this.name;
    }

    public Number getPrice(){
        return this.price;
    }
}