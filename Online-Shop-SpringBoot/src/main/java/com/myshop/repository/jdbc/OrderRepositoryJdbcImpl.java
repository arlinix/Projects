package com.myshop.repository.jdbc;

import com.myshop.model.DefaultOrder;
import com.myshop.model.DefaultProduct;
import com.myshop.repository.OrderRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryJdbcImpl implements OrderRepository {

    @Override
    public int saveOrder(Connection conn, int userId, double totalPrice, String creditCardNumber) throws Exception {
        String sql = "INSERT INTO orders(user_id, total_price, credit_card_number) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setDouble(2, totalPrice);
            ps.setString(3, creditCardNumber);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new Exception("Failed to generate order id");
                }
            }
        }
    }

    @Override
    public void saveOrderItems(Connection conn, int orderId, List<OrderItem> items) throws Exception {
        String sql = "INSERT INTO order_items(order_id, product_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (OrderItem it : items) {
                ps.setInt(1, orderId);
                ps.setInt(2, it.productId);
                ps.setInt(3, it.quantity);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public List<DefaultOrder> findOrdersByUserId(Connection conn, int userId) throws Exception {
        String sql = "SELECT o.id AS oid, o.total_price, o.credit_card_number, oi.product_id, oi.quantity, p.product_name, p.category_name, p.price " +
                "FROM orders o JOIN order_items oi ON o.id = oi.order_id JOIN products p ON oi.product_id = p.id " +
                "WHERE o.user_id = ? ORDER BY o.order_time DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<DefaultOrder> result = new ArrayList<>();
                int currentOrderId = -1;
                DefaultOrder current = null;
                List<DefaultProduct> products = null;
                while (rs.next()) {
                    int oid = rs.getInt("oid");
                    if (oid != currentOrderId) {
                        // finish previous
                        if (current != null) {
                            current.setProducts(products.toArray(new com.myshop.model.Product[0]));
                            result.add(current);
                        }
                        currentOrderId = oid;
                        current = new DefaultOrder();
                        current.setCustomerId(userId);
                        // credit card not set for security in toString, but we set it
                        current.setCreditCardNumber(rs.getString("credit_card_number"));
                        products = new ArrayList<>();
                    }
                    DefaultProduct p = new DefaultProduct(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getString("category_name"),
                            rs.getDouble("price")
                    );
                    // quantity is ignored in DefaultProduct; we'll add product multiple times
                    int qty = rs.getInt("quantity");
                    for (int i = 0; i < qty; i++) products.add(p);
                }
                if (current != null) {
                    current.setProducts(products.toArray(new com.myshop.model.Product[0]));
                    result.add(current);
                }
                return result;
            }
        }
    }
}