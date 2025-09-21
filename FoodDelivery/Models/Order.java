package FoodDelivery.Models;

import java.util.List;

import FoodDelivery.MenuItem.MenuItem;
import FoodDelivery.User.User;

public class Order {
    private static int orderCounter = 1;
    private int orderId;
    private User user;
    private Restaurant restaurant;
    private List<MenuItem> items;
    private int totalAmount;
    private String status; // CREATED, PAID, OUT_FOR_DELIVERY, DELIVERED

    public Order(User user, Restaurant restaurant, List<MenuItem> items, int totalAmount) {
        this.orderId = orderCounter++;
        this.user = user;
        this.restaurant = restaurant;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = "CREATED";
    }

    public int getOrderId() { return orderId; }
    public User getUser() { return user; }
    public Restaurant getRestaurant() { return restaurant; }
    public List<MenuItem> getItems() { return items; }
    public int getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }
}