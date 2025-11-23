package com.myshop.service;

import com.myshop.db.DbConnectionFactory;
import com.myshop.repository.CartRepository;
import com.myshop.repository.OrderRepository;
import com.myshop.repository.UserRepository;
import com.myshop.repository.ProductRepository;
import com.myshop.repository.CartRepository.CartItem;
import com.myshop.repository.OrderRepository.OrderItem;
import com.myshop.repository.jdbc.CartRepositoryJdbcImpl;
import com.myshop.repository.jdbc.OrderRepositoryJdbcImpl;
import com.myshop.repository.jdbc.ProductRepositoryJdbcImpl;
import com.myshop.repository.jdbc.UserRepositoryJdbcImpl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Service that places order in a single JDBC transaction.
 */
public class OrderService {

    private final UserRepository userRepo = new UserRepositoryJdbcImpl();
    private final ProductRepository productRepo = new ProductRepositoryJdbcImpl();
    private final CartRepository cartRepo = new CartRepositoryJdbcImpl();
    private final OrderRepository orderRepo = new OrderRepositoryJdbcImpl();

    /**
     * Place order for userId using creditCardNumber.
     * Steps:
     *  - Read cart items
     *  - Compute total
     *  - Insert into orders
     *  - Insert order_items
     *  - Clear cart
     *
     * All done inside a single DB transaction. Rolls back on failure.
     *
     * Returns created order id.
     */
    public int placeOrder(int userId, String creditCardNumber) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);

                // ensure user exists (optional)
                if (userRepo.findById(conn, userId) == null) {
                    throw new IllegalArgumentException("User not found: " + userId);
                }

                // get cart items
                List<CartItem> items = cartRepo.getCartItems(conn, userId);
                if (items.isEmpty()) {
                    throw new IllegalStateException("Cart is empty for user: " + userId);
                }

                // compute total and prepare order items
                double total = 0.0;
                List<OrderItem> orderItems = new ArrayList<>();
                for (CartItem ci : items) {
                    total += ci.price * ci.quantity;
                    orderItems.add(new OrderItem(ci.productId, ci.quantity));
                }

                // create order
                int orderId = orderRepo.saveOrder(conn, userId, total, creditCardNumber);

                // save order items
                orderRepo.saveOrderItems(conn, orderId, orderItems);

                // clear cart
                cartRepo.clearCart(conn, userId);

                // commit
                conn.commit();
                return orderId;
            } catch (Exception e) {
                // rollback and rethrow
                try { conn.rollback(); } catch (Exception ex) { /* log */ }
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
