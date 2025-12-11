package com.myshop.repository.jdbc;

import com.myshop.model.DefaultUser;
import com.myshop.model.Role;
import com.myshop.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryJdbcImpl implements UserRepository {

    private final DataSource ds;

    @Autowired
    public UserRepositoryJdbcImpl(DataSource ds) {
        this.ds = ds;
    }

    private Connection conn() throws SQLException { return ds.getConnection(); }

    @Override
    public int save(Connection conn, DefaultUser user) throws Exception {
        String sql = "INSERT INTO users(first_name, last_name, email, role, password_hash, is_active) VALUES (?,?,?,?,?,1)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getRole() == null ? Role.CUSTOMER.name() : user.getRole().name());
            ps.setString(5, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No generated key after inserting user");
    }

    // convenience overload
    public int save(DefaultUser user) throws Exception {
        try (Connection conn = conn()) { return save(conn, user); }
    }

    @Override
    public DefaultUser findById(Connection conn, int id) throws Exception {
        String sql = "SELECT id, first_name, last_name, email, role FROM users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapUser(rs);
                return null;
            }
        }
    }

    public DefaultUser findById(int id) throws Exception {
        try (Connection conn = conn()) { return findById(conn, id); }
    }

    @Override
    public DefaultUser findByEmail(Connection conn, String email) throws Exception {
        String sql = "SELECT id, first_name, last_name, email, role FROM users WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapUser(rs);
                return null;
            }
        }
    }

    public DefaultUser findByEmail(String email) throws Exception {
        try (Connection conn = conn()) { return findByEmail(conn, email); }
    }

    @Override
    public List<DefaultUser> findAll(Connection conn) throws Exception {
        String sql = "SELECT id, first_name, last_name, email, role FROM users";
        List<DefaultUser> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapUser(rs));
        }
        return list;
    }

    @Override
    public boolean update(Connection conn, DefaultUser user) throws Exception {
        String sql = "UPDATE users SET first_name=?, last_name=?, email=?, role=?, password_hash=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getRole() == null ? Role.CUSTOMER.name() : user.getRole().name());

            String hash = (user.getPassword()!=null && !user.getPassword().isEmpty())
                    ? BCrypt.hashpw(user.getPassword(), BCrypt.gensalt())
                    : getPasswordHashById(conn, user.getId());
            ps.setString(5, hash);

            ps.setInt(6, user.getId());
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

    private DefaultUser mapUser(ResultSet rs) throws Exception {
        DefaultUser u = new DefaultUser();
        u.setId(rs.getInt("id"));
        u.setFirstName(rs.getString("first_name"));
        u.setLastName(rs.getString("last_name"));
        u.setEmail(rs.getString("email"));
        u.setRole(Role.valueOf(rs.getString("role")));
        return u;
    }

    public String getPasswordHashById(Connection conn, int id) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement("SELECT password_hash FROM users WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
            }
        }
        return null;
    }

    // helper: get hash by email
    public String getPasswordHashByEmail(String email) throws Exception {
        try (Connection conn = conn(); PreparedStatement ps = conn.prepareStatement("SELECT password_hash FROM users WHERE email=?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return rs.getString(1); }
        }
        return null;
    }
}
