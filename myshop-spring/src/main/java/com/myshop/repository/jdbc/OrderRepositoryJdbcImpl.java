
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
        // Do NOT store raw PAN; set NULL (or masked token if you later decide)
        String sql = "INSERT INTO orders(user_id, total_price) " +
                "VALUES (?,?)";
        try (PreparedStatement ps = conn.prepareStatement
                (sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setDouble(2, totalPrice);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                throw new Exception("Failed to generate order id");
            }
        }
    }

    @Override
    public void saveOrderItems(Connection conn, int orderId, List<OrderItem> items) throws Exception {
        String sql = "INSERT INTO order_items(order_id, product_id, quantity) VALUES (?,?,?)";
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
        String sql = "SELECT o.id AS oid, o.total_price, o.status, oi.product_id, oi.quantity, p.product_name, p.category_name, p.price " +
                "FROM orders o JOIN order_items oi ON o.id = oi.order_id JOIN products p ON oi.product_id = p.id " +
                "WHERE o.user_id = ? ORDER BY o.order_time DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return mapOrders(rs, userId);
            }
        }
    }

    @Override
    public List<DefaultOrder> findAll(Connection conn) throws Exception {
        String sql = "SELECT o.id AS oid, o.user_id, o.total_price, o.status, oi.product_id, oi.quantity, p.product_name, p.category_name, p.price " +
                "FROM orders o JOIN order_items oi ON o.id = oi.order_id JOIN products p ON oi.product_id = p.id " +
                "ORDER BY o.order_time DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<DefaultOrder> result = new ArrayList<>();
            int currentOrderId = -1;
            DefaultOrder current = null;
            List<DefaultProduct> products = null;
            int currentUserId = -1;
            while (rs.next()) {
                int oid = rs.getInt("oid");
                if (oid != currentOrderId) {
                    if (current != null) {
                        current.setProducts(products.toArray(new com.myshop.model.Product[0]));
                        result.add(current);
                    }
                    currentOrderId = oid;
                    currentUserId = rs.getInt("user_id");
                    current = new DefaultOrder();
                    current.setCustomerId(currentUserId);
                    // status could be stored inside DefaultOrder via toString or an extension (left minimal)
                    products = new ArrayList<>();
                }
                DefaultProduct p = new DefaultProduct(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category_name"),
                        rs.getDouble("price"),
                        0, true
                );
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

    @Override
    public boolean updateOrderStatus(Connection conn, int orderId, String newStatus, Integer actorUserId, String reason) throws Exception {
        String sql = "UPDATE orders SET status=?, processed_by=IF(? IS NULL, processed_by, ?), " +
                "cancelled_by=IF(? IS NULL, cancelled_by, ?), cancellation_reason=?, updated_at=NOW() WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setObject(2, actorUserId);
            ps.setObject(3, actorUserId);
            ps.setObject(4, actorUserId);
            ps.setObject(5, actorUserId);
            ps.setString(6, reason);
            ps.setInt(7, orderId);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public List<OrderItem> getOrderItems(Connection conn, int orderId) throws Exception {
        String sql = "SELECT product_id, quantity FROM order_items WHERE order_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                List<OrderItem> items = new ArrayList<>();
                while (rs.next()) items.add(new OrderItem(rs.getInt(1), rs.getInt(2)));
                return items;
            }
        }
    }

    // Helper mapper (similar to your existing code)
    private List<DefaultOrder> mapOrders(ResultSet rs, int userId) throws Exception {
        List<DefaultOrder> result = new ArrayList<>();
        int currentOrderId = -1;
        DefaultOrder current = null;
        List<DefaultProduct> products = null;
        while (rs.next()) {
            int oid = rs.getInt("oid");
            if (oid != currentOrderId) {
                if (current != null) {
                    current.setProducts(products.toArray(new com.myshop.model.Product[0]));
                    result.add(current);
                }
                currentOrderId = oid;
                current = new DefaultOrder();
                current.setCustomerId(userId);
                products = new ArrayList<>();
            }
            DefaultProduct p = new DefaultProduct(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getString("category_name"),
                    rs.getDouble("price"),
                    0, true
            );
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
