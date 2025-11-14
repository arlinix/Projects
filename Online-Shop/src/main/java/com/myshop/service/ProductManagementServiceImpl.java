package com.myshop.service;

import com.myshop.model.Product;
import com.myshop.model.DefaultProduct;

import java.util.ArrayList;
import java.util.List;

public class ProductManagementServiceImpl implements ProductManagementService {

    private static ProductManagementServiceImpl instance;
    private List<Product> products = new ArrayList<>();

    ProductManagementServiceImpl() {
        instance = new ProductManagementServiceImpl();
        initDefaultProducts();
    }

    private void initDefaultProducts() {
        products.clear();
        products.add(new DefaultProduct(1, "USB-C Cable", "Electronics", 299.00));
        products.add(new DefaultProduct(2, "Wireless Mouse", "Electronics", 799.00));
        products.add(new DefaultProduct(3, "Notebook", "Stationery", 49.50));
    }


    @Override
    public Product[] getProducts() {
        return products.toArray(new Product[0]);
    }

    @Override
    public Product getProductById(int productIdToAddToCart) {

        return products.stream()
                .filter(p -> p.getId() == productIdToAddToCart)
                .findFirst().orElse(null);
    }

    public void clearServiceState(){
        initDefaultProducts();
    }
}
