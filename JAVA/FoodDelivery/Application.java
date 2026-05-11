package FoodDelivery;

import java.util.List;

import FoodDelivery.Factories.RestrauntFactory;
import FoodDelivery.Managers.CartManager;
import FoodDelivery.Managers.PaymentManager;
import FoodDelivery.Managers.DeliveryManager;
import FoodDelivery.Managers.RestrauntManager;
import FoodDelivery.Models.Cart;
import FoodDelivery.Models.Order;
import FoodDelivery.Models.Restaurant;
import FoodDelivery.User.User;

public class Application {
    private User user;
    private CartManager cartManager = new CartManager();
    private PaymentManager paymentManager = new PaymentManager();
    private DeliveryManager deliveryManager = new DeliveryManager();

    public User login(String name) {
        this.user = new User(name);
        return this.user;
    }

    public static List<String> getAllRestraunts() {
        RestrauntManager rm = new RestrauntManager();
        return rm.getAllRestrauntsNames();
    }

    public User getCurrentUser() {
        return user;
    }

    public Restaurant getRestraunt(String name) {
        RestrauntFactory rf = new RestrauntFactory();
        return rf.getRestraunt(name);
    }

    public Cart addItemInCart(String restraunt, String item) {
        cartManager.addItemInCart(restraunt, item);
        return null;
    }

    public void placeOrder(Restaurant restaurant, Cart cart){
        Order order = cartManager.checkout(user, restaurant, cart);
        if(paymentManager.processPayment(order, "UPI")) {
            deliveryManager.assignDelivery(order);
            deliveryManager.completeDelivery(order);
        }
    }
}