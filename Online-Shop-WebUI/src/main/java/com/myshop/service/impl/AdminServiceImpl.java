
package com.myshop.service.impl;

import com.myshop.db.DbConnectionFactory;
import com.myshop.model.DefaultProduct;
import com.myshop.repository.ProductRepository;
import com.myshop.repository.OrderRepository;
import com.myshop.repository.UserRepository;
import com.myshop.repository.jdbc.ProductRepositoryJdbcImpl;
import com.myshop.repository.jdbc.OrderRepositoryJdbcImpl;
import com.myshop.repository.jdbc.UserRepositoryJdbcImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AdminServiceImpl implements com.myshop.service.AdminService {
    private final ProductRepository productRepo = new ProductRepositoryJdbcImpl();
    private final OrderRepository orderRepo = new OrderRepositoryJdbcImpl();
    private final UserRepository userRepo = new UserRepositoryJdbcImpl();

    @Override
    public int createProduct(String name, String category, double price, int stock) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            DefaultProduct p = new DefaultProduct(0, name, category, price, stock, true);
            return productRepo.save(conn, p);
        }
    }

    @Override
    public boolean updateProduct(int id, String name, String category, double price, int stock, boolean active) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            DefaultProduct p = productRepo.findById(conn, id);
            if (p == null) return false;
            p = new DefaultProduct(id, name, category, price, stock, active);
            return productRepo.update(conn, p);
        }
    }

    @Override
    public boolean deleteProduct(int id) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            // Hard delete; consider soft delete by setting is_active = 0
            return productRepo.delete(conn, id);
        }
    }

    @Override
    public boolean processOrder(int orderId, int adminUserId) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            return orderRepo.updateOrderStatus(conn, orderId, "PROCESSING", adminUserId, null);
        }
    }

    @Override
    public boolean shipOrder(int orderId, int adminUserId) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            return orderRepo.updateOrderStatus(conn, orderId, "SHIPPED", adminUserId, null);
        }
    }

    @Override
    public boolean cancelOrder(int orderId, int adminUserId, String reason) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            // restock quantities from order_items
            var items = orderRepo.getOrderItems(conn, orderId);
            for (OrderRepository.OrderItem it : items) {
                try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE products SET stock_quantity = stock_quantity + ? WHERE id=?")) {
                    ps.setInt(1, it.quantity);
                    ps.setInt(2, it.productId);
                    ps.executeUpdate();
                }
            }
            return orderRepo.updateOrderStatus(conn, orderId, "CANCELLED", adminUserId, reason);
        }
    }

    @Override
    public boolean resetUserEmail(int targetUserId, String newEmail, int adminUserId) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            var u = userRepo.findById(conn, targetUserId);
            if (u == null) return false;
            u.setEmail(newEmail);
            boolean ok = userRepo.update(conn, u);
            // audit
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO audits(actor_user_id, action, target, details) VALUES(?,?,?,?)")) {
                ps.setInt(1, adminUserId);
                ps.setString(2, "USER_RESET_EMAIL");
                ps.setString(3, "user:" + targetUserId);
                ps.setString(4, "newEmail=" + newEmail);
                ps.executeUpdate();
            }
            return ok;
        }
    }

    @Override
    public boolean resetUserPassword(int targetUserId, String newTempPassword, int adminUserId) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            var u = userRepo.findById(conn, targetUserId);
            if (u == null) return false;
            u.setPassword(newTempPassword); // repo will re-hash on update
            boolean ok = userRepo.update(conn, u);
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO audits(actor_user_id, action, target, details) VALUES(?,?,?,?)")) {
                ps.setInt(1, adminUserId);
                ps.setString(2, "USER_RESET_PASSWORD");
                ps.setString(3, "user:" + targetUserId);
                ps.setString(4, "temporary password set");
                ps.executeUpdate();
            }
            return ok;
        }
    }
}
