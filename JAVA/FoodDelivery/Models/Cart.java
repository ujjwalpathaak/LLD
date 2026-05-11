package FoodDelivery.Models;

import java.util.ArrayList;
import java.util.List;
import FoodDelivery.MenuItem.MenuItem;

public class Cart {
    private int total = 0;
    private String restraunt;
    private List<MenuItem> items = new ArrayList<>();

    public Cart(String restraunt) {
        this.restraunt = restraunt;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void addItem(String itemName) {
        MenuItem item = new MenuItem(itemName, 0);
        items.add(item);
    }

    public String getRestraunt() {
        return restraunt;
    }

    public int getTotal() {
        return total;
    }
}