package com.myshop.model;

public interface Cart {

    boolean isEmpty();

    void addProduct(Product productById);

    Product[] getProducts();

    void clear();

}
