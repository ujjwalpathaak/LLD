package FoodDelivery;

import FoodDelivery.Models.Cart;
import FoodDelivery.Models.Restaurant;

public class Main {
    public static void main(String[] args) {
        Application app = new Application();
        app.login("Ujjwal");

        Restaurant fastFood = app.getRestraunt("FastFood");
        app.addItemInCart("FastFood", "Pizza");
        app.addItemInCart("FastFood", "Burger");

        Cart cart = new Cart("FastFood");
        app.placeOrder(fastFood, cart);
    }
}