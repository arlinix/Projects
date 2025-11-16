package com.myshop.service.impl;

import com.myshop.model.Product;
import com.myshop.repository.ProductRepository;
import com.myshop.model.DefaultProduct;
import com.myshop.service.ProductManagementService;

public class ProductManagementServiceImpl implements ProductManagementService {

    private final ProductRepository productRepository;

    public ProductManagementServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
        initDefaults();
    }

    private void initDefaults() {
        productRepository.clear();
        productRepository.save(new DefaultProduct(1, "USB-C Cable", "Electronics", 299.00));
        productRepository.save(new DefaultProduct(2, "Wireless Mouse", "Electronics", 799.00));
        productRepository.save(new DefaultProduct(3, "Notebook", "Stationery", 49.50));
    }

    @Override public Product[] getProducts() { return productRepository.findAll(); }

    @Override public Product getProductById(int productIdToAddToCart) { return productRepository.findById(productIdToAddToCart); }

    @Override public void clearServiceState() { initDefaults(); }

    // helper allowing loader to add more products
    public void addProduct(Product p) { productRepository.save(p); }
}
