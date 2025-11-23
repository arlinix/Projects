
package com.myshop.repository.jdbc;

import com.myshop.model.DefaultProduct;
import com.myshop.repository.ProductRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryJdbcImpl implements ProductRepository {

    @Override
    public int save(Connection conn, DefaultProduct product) throws Exception {
        String sql = "INSERT INTO products(product_name, category_name, price, stock_quantity, is_active) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getCategoryName());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStockQuantity());     // NEW
            ps.setBoolean(5, product.isActive());         // NEW
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                throw new SQLException("No generated key for product");
            }
        }
    }

    @Override
    public DefaultProduct findById(Connection conn, int id) throws Exception {
        String sql = "SELECT id, product_name, category_name, price, stock_quantity, is_active FROM products WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DefaultProduct(
                            rs.getInt("id"),
                            rs.getString("product_name"),
                            rs.getString("category_name"),
                            rs.getDouble("price"),
                            rs.getInt("stock_quantity"),
                            rs.getBoolean("is_active")
                    );
                }
                return null;
            }
        }
    }

    @Override
    public DefaultProduct findByName(Connection conn, String name) throws Exception {
        String sql = "SELECT id, product_name, category_name, price, stock_quantity, is_active FROM products WHERE product_name = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DefaultProduct(
                            rs.getInt("id"),
                            rs.getString("product_name"),
                            rs.getString("category_name"),
                            rs.getDouble("price"),
                            rs.getInt("stock_quantity"),
                            rs.getBoolean("is_active")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<DefaultProduct> findAll(Connection conn) throws Exception {
        String sql = "SELECT id, product_name, category_name, price, stock_quantity, is_active FROM products";
        List<DefaultProduct> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DefaultProduct(
                        rs.getInt("id"),
                        rs.getString("product_name"),
                        rs.getString("category_name"),
                        rs.getDouble("price"),
                        rs.getInt("stock_quantity"),
                        rs.getBoolean("is_active")
                ));
            }
        }
        return list;
    }

    @Override
    public boolean update(Connection conn, DefaultProduct product) throws Exception {
        String sql = "UPDATE products SET product_name=?, category_name=?, price=?, stock_quantity=?, is_active=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getCategoryName());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStockQuantity());
            ps.setBoolean(5, product.isActive());
            ps.setInt(6, product.getId());
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean delete(Connection conn, int id) throws Exception {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public int count(Connection conn) throws Exception {
        String sql = "SELECT COUNT(*) FROM products";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
            return 0;
        }
    }
}
