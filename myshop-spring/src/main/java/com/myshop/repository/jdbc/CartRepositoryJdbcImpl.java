package com.myshop.repository.jdbc;

import com.myshop.repository.CartRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartRepositoryJdbcImpl implements com.myshop.repository.CartRepository {

    @Override
    public void ensureCartExists(Connection conn, int userId) throws Exception {
        String check = "SELECT user_id FROM carts WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    String insert = "INSERT INTO carts(user_id) VALUES(?)";
                    try (PreparedStatement ps2 = conn.prepareStatement(insert)) {
                        ps2.setInt(1, userId);
                        ps2.executeUpdate();
                    }
                }
            }
        }
    }

    @Override
    public void addItem(Connection conn, int userId, int productId, int qty) throws Exception {
        ensureCartExists(conn, userId);
        // if product already in cart, update qty
        String select = "SELECT id, quantity FROM cart_items WHERE cart_user_id = ? AND product_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(select)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int existing = rs.getInt("quantity");
                    String update = "UPDATE cart_items SET quantity = ? WHERE id = ?";
                    try (PreparedStatement ps2 = conn.prepareStatement(update)) {
                        ps2.setInt(1, existing + qty);
                        ps2.setInt(2, id);
                        ps2.executeUpdate();
                    }
                } else {
                    String insert = "INSERT INTO cart_items(cart_user_id, product_id, quantity) VALUES (?, ?, ?)";
                    try (PreparedStatement ps2 = conn.prepareStatement(insert)) {
                        ps2.setInt(1, userId);
                        ps2.setInt(2, productId);
                        ps2.setInt(3, qty);
                        ps2.executeUpdate();
                    }
                }
            }
        }
    }

    @Override
    public void removeItem(Connection conn, int userId, int productId) throws Exception {
        String sql = "DELETE FROM cart_items WHERE cart_user_id = ? AND product_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        }
    }

    @Override
    public void updateQuantity(Connection conn, int userId, int productId, int qty) throws Exception {
        String sql = "UPDATE cart_items SET quantity = ? WHERE cart_user_id = ? AND product_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        }
    }

    @Override
    public List<CartItem> getCartItems(Connection conn, int userId) throws Exception {
        String sql = "SELECT ci.product_id, p.product_name, p.category_name, p.price, ci.quantity " +
                     "FROM cart_items ci JOIN products p ON ci.product_id = p.id " +
                     "WHERE ci.cart_user_id = ?";
        List<CartItem> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new CartItem(
                            rs.getInt("product_id"),
                            rs.getString("product_name"),
                            rs.getString("category_name"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    ));
                }
            }
        }
        return list;
    }

    @Override
    public void clearCart(Connection conn, int userId) throws Exception {
        String sql = "DELETE FROM cart_items WHERE cart_user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }
}
