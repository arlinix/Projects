package com.myshop.util;

import com.myshop.app.ApplicationContext;
import com.myshop.core.Shop;
import com.myshop.model.DefaultProduct;
import com.myshop.service.impl.ProductManagementServiceImpl;
import com.myshop.service.impl.UserManagementServiceImpl;
import com.myshop.service.impl.OrderManagementServiceImpl;

public class ShopLoader {

    private ShopLoader() {}

    public static Shop loadShop(ApplicationContext ctx) {
        // ensure services are reset for a fresh run
        UserManagementServiceImpl.getInstance().clearServiceState();
        ProductManagementServiceImpl.getInstance().clearServiceState();
        OrderManagementServiceImpl.getInstance().clearServiceState();

        // optionally preload some extra products
        ProductManagementServiceImpl.getInstance().addProduct(new DefaultProduct(10, "Laptop", "Electronics", 55000));
        ProductManagementServiceImpl.getInstance().addProduct(new DefaultProduct(11, "Shoes", "Fashion", 2500));

        // create admin user
        UserManagementServiceImpl.getInstance().registerUser(new com.myshop.model.DefaultUser() {{
            setFirstName("Admin");
            setLastName("User");
            setEmail("admin@gmail.com");
            setPassword("admin123");
        }});

        // build and return the Shop wired with the context
        return new Shop(
                ctx.getUserService(),
                ctx.getProductService(),
                ctx.getOrderService(),
                ctx
        );
    }
}
