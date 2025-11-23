
package com.myshop.service;

import com.myshop.db.DbConnectionFactory;
import com.myshop.repository.CartRepository;
import com.myshop.repository.OrderRepository;
import com.myshop.repository.CartRepository.CartItem;
import com.myshop.repository.OrderRepository.OrderItem;
import com.myshop.repository.jdbc.CartRepositoryJdbcImpl;
import com.myshop.repository.jdbc.OrderRepositoryJdbcImpl;
import com.myshop.repository.jdbc.ProductRepositoryJdbcImpl;
import com.myshop.repository.NotificationRepository;
import com.myshop.repository.jdbc.NotificationRepositoryJdbcImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Service that places order in a single JDBC transaction, with stock checks.
 */
public class OrderService {
    private final CartRepository cartRepo = new CartRepositoryJdbcImpl();
    private final OrderRepository orderRepo = new OrderRepositoryJdbcImpl();
    private final NotificationRepository notificationRepo = new NotificationRepositoryJdbcImpl();

    /**
     * Place order for userId using creditCardNumber.
     * - Validates user cart
     * - Locks product rows (SELECT ... FOR UPDATE)
     * - Ensures stock availability, notifies stock-out if needed
     * - Inserts order (credit_card_number saved as NULL), items
     * - Decrements stock atomically
     * - Clears cart
     */
    public int placeOrder(int userId, String creditCardNumber) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);

                // 1) get cart items
                List<CartItem> items = cartRepo.getCartItems(conn, userId);
                if (items.isEmpty()) throw new IllegalStateException("Cart is empty for user: " + userId);

                // 2) lock & check stock
                for (CartItem ci : items) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "SELECT stock_quantity FROM products WHERE id=? FOR UPDATE")) {
                        ps.setInt(1, ci.productId);
                        try (var rs = ps.executeQuery()) {
                            if (!rs.next()) throw new IllegalStateException("Product not found: " + ci.productId);
                            int stock = rs.getInt(1);
                            if (stock < ci.quantity) {
                                // notify and abort
                                com.myshop.model.Notification n = new com.myshop.model.Notification();
                                n.setUserId(userId);
                                n.setMessage("Stock out: " + ci.productName + " (requested " + ci.quantity + ", available " + stock + ")");
                                notificationRepo.add(conn, n);
                                throw new IllegalStateException("Stock out for " + ci.productName);
                            }
                        }
                    }
                }

                // 3) compute total
                double total = 0.0;
                List<OrderItem> orderItems = new ArrayList<>();
                for (CartItem ci : items) {
                    total += ci.price * ci.quantity;
                    orderItems.add(new OrderItem(ci.productId, ci.quantity));
                }

                // 4) create order (do not store PAN)
                int orderId = orderRepo.saveOrder(conn, userId, total, null);

                // 5) save items
                orderRepo.saveOrderItems(conn, orderId, orderItems);

                // 6) decrement stock
                for (CartItem ci : items) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id=?")) {
                        ps.setInt(1, ci.quantity);
                        ps.setInt(2, ci.productId);
                        ps.executeUpdate();
                    }
                }

                // 7) clear cart
                cartRepo.clearCart(conn, userId);

                conn.commit();
                return orderId;
            } catch (Exception e) {
                try { conn.rollback(); } catch (Exception ig) {}
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
