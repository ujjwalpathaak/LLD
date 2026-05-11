package FoodDelivery.Models;

import java.util.List;

import FoodDelivery.Menu.Menu;

public class Restaurant {
    private static int idCounter = 1;
    private int id;
    private String name;
    private Menu menu;

    public Restaurant(String name, Menu menu) {
        this.id = idCounter++;
        this.name = name;
        this.menu = menu;
    }

    public String getName(){
        return this.name;
    }

    public Menu getMenu(){
        return this.menu;
    }
}