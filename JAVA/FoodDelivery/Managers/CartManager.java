package FoodDelivery.Managers;

import java.util.ArrayList;
import java.util.List;

import FoodDelivery.Models.Cart;
import FoodDelivery.Models.Order;
import FoodDelivery.Models.Restaurant;
import FoodDelivery.MenuItem.MenuItem;
import FoodDelivery.User.User;

public class CartManager {
    List<Cart> carts = new ArrayList<>();

    public Cart addItemInCart(String restraunt, String item) {
    Cart cart = getInstance(restraunt);
    if (cart == null) {
        cart = createNewCart(restraunt);
        carts.add(cart);
    }
    cart.addItem(item);
    return cart;
}

    private Cart getInstance(String restraunt){
        for (Cart cart : carts){
            if(cart.getRestraunt().equals(restraunt)){
                return cart;
            }
        }
        return null;
    }

    private Cart createNewCart(String restraunt) {
    return new Cart(restraunt);
}

    public Order checkout(User user, Restaurant restaurant, Cart cart){
        Order order = new Order(user, restaurant, cart.getItems(), cart.getTotal());
        return order;
    }
}