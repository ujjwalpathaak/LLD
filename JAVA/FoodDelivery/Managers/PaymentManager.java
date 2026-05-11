package FoodDelivery.Managers;

import FoodDelivery.Models.Order;

public class PaymentManager {

    public boolean processPayment(Order order, String paymentMethod) {
        System.out.println("Processing payment of Rs." + order.getTotalAmount() + " via " + paymentMethod);

        // for now, assume payment is always successful
        order.setStatus("PAID");
        System.out.println("Payment successful for Order ID: " + order.getOrderId());
        return true;
    }
}