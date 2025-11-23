package com.myshop.repository;

import com.myshop.model.DefaultProduct;

import java.sql.Connection;
import java.util.List;

public interface CartRepository {
    void ensureCartExists(Connection conn, int userId) throws Exception;
    void addItem(Connection conn, int userId, int productId, int qty) throws Exception;
    void removeItem(Connection conn, int userId, int productId) throws Exception;
    void updateQuantity(Connection conn, int userId, int productId, int qty) throws Exception;
    List<CartItem> getCartItems(Connection conn, int userId) throws Exception;
    void clearCart(Connection conn, int userId) throws Exception;

    // helper DTO
    class CartItem {
        public final int productId;
        public final String productName;
        public final String categoryName;
        public final double price;
        public final int quantity;

        public CartItem(int productId, String productName, String categoryName, double price, int quantity) {
            this.productId = productId;
            this.productName = productName;
            this.categoryName = categoryName;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
