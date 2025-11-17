package com.myshop.service.impl;

import com.myshop.db.DbConnectionFactory;
import com.myshop.model.DefaultProduct;
import com.myshop.model.Product;
import com.myshop.repository.ProductRepository;
import com.myshop.service.ProductManagementService;

import java.sql.Connection;
import java.util.List;

public class ProductManagementServiceImpl implements ProductManagementService {

    private final ProductRepository productRepository;

    public ProductManagementServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
        try { initDefaults(); } catch (Exception ex) { /* log */ }
    }

    private void initDefaults() throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            int cnt = productRepository.count(conn);
            if (cnt == 0) {
                productRepository.save(conn, new DefaultProduct(0, "USB-C Cable", "Electronics", 299.00));
                productRepository.save(conn, new DefaultProduct(0, "Wireless Mouse", "Electronics", 799.00));
                productRepository.save(conn, new DefaultProduct(0, "Notebook", "Stationery", 49.50));
            }
        }
    }

    @Override
    public Product[] getProducts() {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            List<DefaultProduct> list = productRepository.findAll(conn);
            return list.toArray(new Product[0]);
        } catch (Exception e) {
            return new Product[0];
        }
    }

    @Override
    public Product getProductById(int productIdToAddToCart) {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            return productRepository.findById(conn, productIdToAddToCart);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void clearServiceState() {
        // no-op for DB-backed
    }

    public void addProduct(DefaultProduct p) throws Exception {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            productRepository.save(conn, p);
        }
    }
}