package com.myshop.repository;

import com.myshop.model.Product;

public interface ProductRepository {
    void save(Product product);
    Product findById(int id);
    Product[] findAll();
    void clear();
}
