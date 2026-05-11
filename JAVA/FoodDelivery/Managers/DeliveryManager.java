package FoodDelivery.Managers;

import FoodDelivery.Models.Order;

public class DeliveryManager {

    public void assignDelivery(Order order) {
        System.out.println("Assigning delivery agent for Order ID: " + order.getOrderId());
        order.setStatus("OUT_FOR_DELIVERY");
    }

    public void completeDelivery(Order order) {
        System.out.println("Order ID " + order.getOrderId() + " delivered successfully!");
        order.setStatus("DELIVERED");
    }
}