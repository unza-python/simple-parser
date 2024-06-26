/*
 * This source file was generated by the Gradle 'init' task
 */
package org.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderTrackerTest {

    @Test
    void testCreateOrder() {
        OrderTracker tracker = new OrderTracker();
        tracker.createOrder("ORD001");
        assertEquals(1, tracker.getOrders().size(), "One order should be created");
    }

    @Test
    void testUpdateOrderStatus() {
        OrderTracker tracker = new OrderTracker();
        tracker.createOrder("ORD001");
        tracker.updateOrderStatus("ORD001", "Shipped");
        assertEquals("Shipped", tracker.getOrders().get(0).getStatus(), "Order status should be updated to Shipped");
    }

    @Test
    void testUpdateOrderStatusOrderNotFound() {
        OrderTracker tracker = new OrderTracker();
        tracker.createOrder("ORD001");
        tracker.updateOrderStatus("ORD002", "Shipped");
        assertEquals("Pending", tracker.getOrders().get(0).getStatus(), "Order status should remain Pending if order not found");
    }
}
