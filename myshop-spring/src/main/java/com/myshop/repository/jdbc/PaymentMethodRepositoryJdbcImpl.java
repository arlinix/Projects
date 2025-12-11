
package com.myshop.repository.jdbc;

import com.myshop.model.PaymentMethod;
import com.myshop.repository.PaymentMethodRepository;

import java.sql.*;

public class PaymentMethodRepositoryJdbcImpl implements PaymentMethodRepository {
    @Override
    public int upsertDefault(Connection conn, PaymentMethod pm) throws Exception {
        // Clear old defaults
        try (PreparedStatement ps = conn.prepareStatement(
            "UPDATE payment_methods SET is_default=0 WHERE user_id=?")) {
            ps.setInt(1, pm.getUserId());
            ps.executeUpdate();
        }

        // Try find by token for user
        Integer existingId = null;
        try (PreparedStatement ps = conn.prepareStatement(
            "SELECT id FROM payment_methods WHERE user_id=? AND token=?")) {
            ps.setInt(1, pm.getUserId());
            ps.setString(2, pm.getToken());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) existingId = rs.getInt(1);
            }
        }

        if (existingId != null) {
            try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE payment_methods SET brand=?, last4=?, exp_month=?, exp_year=?, is_default=1 WHERE id=?")) {
                ps.setString(1, pm.getBrand());
                ps.setString(2, pm.getLast4());
                ps.setInt(3, pm.getExpMonth());
                ps.setInt(4, pm.getExpYear());
                ps.setInt(5, existingId);
                ps.executeUpdate();
            }
            return existingId;
        } else {
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO payment_methods(user_id, brand, last4, exp_month, exp_year, token, is_default) VALUES(?,?,?,?,?,?,1)",
                Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, pm.getUserId());
                ps.setString(2, pm.getBrand());
                ps.setString(3, pm.getLast4());
                ps.setInt(4, pm.getExpMonth());
                ps.setInt(5, pm.getExpYear());
                ps.setString(6, pm.getToken());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to upsert payment method");
    }

    @Override
    public PaymentMethod findDefaultByUserId(Connection conn, int userId) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
            "SELECT id, brand, last4, exp_month, exp_year, token FROM payment_methods WHERE user_id=? AND is_default=1")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PaymentMethod pm = new PaymentMethod();
                    pm.setId(rs.getInt("id"));
                    pm.setUserId(userId);
                    pm.setBrand(rs.getString("brand"));
                    pm.setLast4(rs.getString("last4"));
                    pm.setExpMonth(rs.getInt("exp_month"));
                    pm.setExpYear(rs.getInt("exp_year"));
                    pm.setToken(rs.getString("token"));
                    pm.setDefault(true);
                    return pm;
                }
            }
        }
        return null;
    }
}
