package FoodDelivery.Menu;

import java.util.ArrayList;
import java.util.List;
import FoodDelivery.MenuItem.MenuItem;

public class Menu {
    private List<MenuItem> items = new ArrayList<>();

    public void addItem(String name, Number price) {
        this.items.add(new MenuItem(name, price));
    }

    public List<MenuItem> getItems() {
        return items;
    }
}