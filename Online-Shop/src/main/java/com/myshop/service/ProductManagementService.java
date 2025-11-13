package com.myshop.service;

import com.myshop.model.Product;

public interface ProductManagementService {
    Product[] getProducts();

    Product getProductById(int productIdToAddToCart);

}
