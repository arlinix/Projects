package com.myshop.repository.impl;

import com.myshop.model.Product;
import com.myshop.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryProductRepository implements ProductRepository {
    private final List<Product> products = new ArrayList<>();

    @Override public void save(Product product) { if (product != null) products.add(product); }
    @Override public Product findById(int id) {
        Optional<Product> opt = products.stream().filter(p -> p.getId() == id).findFirst();
        return opt.orElse(null);
    }
    @Override public Product[] findAll() { return products.toArray(new Product[0]); }
    @Override public void clear() { products.clear(); }
}
