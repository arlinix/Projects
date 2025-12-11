
package com.myshop.repository.jdbc;

import com.myshop.model.Notification;
import com.myshop.repository.NotificationRepository;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepositoryJdbcImpl implements NotificationRepository {
    @Override
    public int add(Connection conn, Notification n) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO notifications(user_id, message) VALUES(?, ?)",
            Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, n.getUserId());
            ps.setString(2, n.getMessage());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("Failed to insert notification");
    }

    @Override
    public List<Notification> listUnreadByUser(Connection conn, int userId) throws Exception {
        List<Notification> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
            "SELECT id, message, created_at FROM notifications WHERE user_id=? AND is_read=0 ORDER BY created_at DESC")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notification n = new Notification();
                    n.setId(rs.getInt("id"));
                    n.setUserId(userId);
                    n.setMessage(rs.getString("message"));
                    n.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                    n.setRead(false);
                    out.add(n);
                }
            }
        }
        return out;
    }

    @Override
    public int markAllRead(Connection conn, int userId) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
            "UPDATE notifications SET is_read=1 WHERE user_id=?")) {
            ps.setInt(1, userId);
            return ps.executeUpdate();
        }
    }
}
