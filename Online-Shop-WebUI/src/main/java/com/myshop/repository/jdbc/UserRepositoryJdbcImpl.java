package com.myshop.repository.jdbc;

import com.myshop.model.DefaultUser;
import com.myshop.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryJdbcImpl implements UserRepository {

    @Override
    public int save(Connection conn, DefaultUser user) throws Exception {
        String sql = "INSERT INTO users(first_name, last_name, password, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    try { user.setId(id); } catch (Exception ignored) {}
                    return id;
                } else {
                    throw new SQLException("No generated key after inserting user");
                }
            }
        }
    }

    @Override
    public DefaultUser findById(Connection conn, int id) throws Exception {
        String sql = "SELECT id, first_name, last_name, password, email FROM users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DefaultUser u = new DefaultUser();
                    u.setId(rs.getInt("id"));
                    u.setFirstName(rs.getString("first_name"));
                    u.setLastName(rs.getString("last_name"));
                    u.setPassword(rs.getString("password"));
                    u.setEmail(rs.getString("email"));
                    return u;
                }
                return null;
            }
        }
    }

    @Override
    public DefaultUser findByEmail(Connection conn, String email) throws Exception {
        String sql = "SELECT id, first_name, last_name, password, email FROM users WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DefaultUser u = new DefaultUser();
                    u.setId(rs.getInt("id"));
                    u.setFirstName(rs.getString("first_name"));
                    u.setLastName(rs.getString("last_name"));
                    u.setPassword(rs.getString("password"));
                    u.setEmail(rs.getString("email"));
                    return u;
                }
                return null;
            }
        }
    }

    @Override
    public List<DefaultUser> findAll(Connection conn) throws Exception {
        String sql = "SELECT id, first_name, last_name, password, email FROM users";
        List<DefaultUser> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DefaultUser u = new DefaultUser();
                u.setId(rs.getInt("id"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setPassword(rs.getString("password"));
                u.setEmail(rs.getString("email"));
                list.add(u);
            }
        }
        return list;
    }

    @Override
    public boolean update(Connection conn, DefaultUser user) throws Exception {
        String sql = "UPDATE users SET first_name=?, last_name=?, password=?, email=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.setInt(5, user.getId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(Connection conn, int id) throws Exception {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }
}