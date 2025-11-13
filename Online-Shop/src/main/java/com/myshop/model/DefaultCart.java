package com.myshop.model;

import java.util.ArrayList;
import java.util.List;

public class DefaultCart implements Cart {
    private List<Product> items = new ArrayList<>();

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public void addProduct(Product productById) {
        if(productById != null){
            items.add(productById);
        }
    }

    @Override
    public Product[] getProducts() {
        return items.toArray(new Product[0]);
    }

    @Override
    public void clear() {
        items.clear();
    }
}
