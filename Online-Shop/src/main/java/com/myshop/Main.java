package com.myshop;

import com.myshop.app.ApplicationContext;
import com.myshop.core.Shop;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = ApplicationContext.getInstance();
        // lazy initialize shop via loader if not already created
        // (In the provided ApplicationContext constructor we call loader automatically)
        Shop shop = ctx.getShop();

        System.out.println("Products in catalog:");
        var products = shop.getAllProducts();
        for (var p : products) {
            System.out.printf("%d: %s (%s) price=%.2f%n", p.getId(), p.getProductName(), p.getCategoryName(), p.getPrice());
        }
    }
}
