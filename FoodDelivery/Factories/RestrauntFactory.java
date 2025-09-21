package FoodDelivery.Factories;

import FoodDelivery.Menu.Menu;
import FoodDelivery.Models.Restaurant;

public class RestrauntFactory {
    public Restaurant getRestraunt (String name) {
        Menu menu = new Menu();
        switch (name) {
            case "FastFood":
                menu.addItem("Pizza", 100);
                menu.addItem("Burger", 80);
                menu.addItem("Pasta", 75);

                Restaurant restaurant = new Restaurant(name, menu);
                return restaurant;
        
            case "Indian":
                menu.addItem("Dal", 60);
                menu.addItem("Rice", 50);
                menu.addItem("Roti", 10);

                restaurant = new Restaurant(name, menu);
                return restaurant;
        
            case "SouthIndian":
                menu.addItem("Idli", 20);
                menu.addItem("Dosa", 80);
                menu.addItem("Vada", 50);

                restaurant = new Restaurant(name, menu);
                return restaurant;
        
            default:
                return null;
        }
    }
}
