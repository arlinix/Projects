
package com.myshop.repository;

import java.sql.Connection;
import java.util.List;

public interface OrderRepository {
    int saveOrder(Connection conn, int userId, double totalPrice, String creditCardNumber) throws Exception;
    void saveOrderItems(Connection conn, int orderId, List<OrderItem> items) throws Exception;
    List<com.myshop.model.DefaultOrder> findOrdersByUserId(Connection conn, int userId) throws Exception;

    // NEW:
    List<com.myshop.model.DefaultOrder> findAll(Connection conn) throws Exception;
    boolean updateOrderStatus(Connection conn, int orderId, String newStatus, Integer actorUserId, String reason) throws Exception;
    List<OrderItem> getOrderItems(Connection conn, int orderId) throws Exception;

    class OrderItem {
        public final int productId;
        public final int quantity;
        public OrderItem(int productId, int quantity) {
            this.productId = productId; this.quantity = quantity;
        }
    }
}
