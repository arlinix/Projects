
package com.myshop.model;

public class DefaultProduct implements Product {
    private final int id;
    private final String productName;
    private final String categoryName;
    private final double price;
    private final int stockQuantity;     // NEW
    private final boolean active;        // NEW

    public DefaultProduct(int id, String productName, String categoryName, double price) {
        this(id, productName, categoryName, price, 0, true);
    }

    public DefaultProduct(int id, String productName, String categoryName, double price, int stockQuantity, boolean active) {
        this.id = id;
        this.productName = productName;
        this.categoryName = categoryName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.active = active;
    }

    @Override public int getId() { return id; }
    @Override public String getProductName() { return productName; }
    @Override public String getCategoryName() { return categoryName; }
    @Override public double getPrice() { return price; }

    // Extra getters for admin screens
    public int getStockQuantity() { return stockQuantity; }
    public boolean isActive() { return active; }

    @Override
    public String toString() {
        return String.format("Product{id=%d, name=%s, cat=%s, price=%.2f, stock=%d, active=%s}",
                id, productName, categoryName, price, stockQuantity, active);
    }
}
