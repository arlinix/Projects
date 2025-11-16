package com.myshop.model;

public class DefaultProduct implements Product {
    private final int id;
    private final String productName;
    private final String categoryName;
    private final double price;

    public DefaultProduct(int id, String productName, String categoryName, double price) {
        this.id = id;
        this.productName = productName;
        this.categoryName = categoryName;
        this.price = price;
    }

    @Override public int getId() { return id; }
    @Override public String getProductName() { return productName; }
    @Override public String getCategoryName() { return categoryName; }
    @Override public double getPrice() { return price; }
}
