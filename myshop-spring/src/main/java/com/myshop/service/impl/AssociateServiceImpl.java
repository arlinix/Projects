
package com.myshop.service.impl;

import com.myshop.db.DbConnectionFactory;
import com.myshop.repository.OrderRepository;
import com.myshop.repository.jdbc.OrderRepositoryJdbcImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AssociateServiceImpl implements com.myshop.service.AssociateService {
    private final OrderRepository orderRepo = new OrderRepositoryJdbcImpl();

    @Override
    public com.myshop.model.Order[] listAllOrders() throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            var list = orderRepo.findAll(conn);
            return list.toArray(new com.myshop.model.Order[0]);
        }
    }

    @Override
    public boolean processOrder(int orderId, int associateUserId) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            return orderRepo.updateOrderStatus(conn, orderId, "PROCESSING", associateUserId, null);
        }
    }

    @Override
    public boolean cancelOrder(int orderId, int associateUserId, String reason) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            // restock items
            var items = orderRepo.getOrderItems(conn, orderId);
            for (OrderRepository.OrderItem it : items) {
                try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE products SET stock_quantity = stock_quantity + ? WHERE id=?")) {
                    ps.setInt(1, it.quantity);
                    ps.setInt(2, it.productId);
                    ps.executeUpdate();
                }
            }
            return orderRepo.updateOrderStatus(conn, orderId, "CANCELLED", associateUserId, reason);
        }
    }
}
