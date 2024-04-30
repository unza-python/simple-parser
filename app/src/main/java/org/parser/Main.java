package org.parser;

import java.util.ArrayList;
import java.util.List;

class Order {
    private String orderId;
    private String status;

    public Order(String orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

class OrderTracker {
    private List<Order> orders;

    public OrderTracker() {
        orders = new ArrayList<>();
    }

    public List<Order> getOrders() {
        return this.orders;
    }
    
    public void createOrder(String orderId) {
        Order newOrder = new Order(orderId, "Pending");
        orders.add(newOrder);
        System.out.println("Order " + orderId + " created successfully.");
    }

    public void updateOrderStatus(String orderId, String newStatus) {
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                order.setStatus(newStatus);
                System.out.println("Order " + orderId + " status updated to " + newStatus);
                return;
            }
        }
        System.out.println("Order " + orderId + " not found.");
    }

    public void displayOrders() {
        System.out.println("All Orders:");
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getOrderId() + ", Status: " + order.getStatus());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        OrderTracker tracker = new OrderTracker();

        // Create some orders
        tracker.createOrder("ORD001");
        tracker.createOrder("ORD002");

        // Update status of an order
        tracker.updateOrderStatus("ORD001", "Shipped");

        // Display all orders
        tracker.displayOrders();
    }
}

