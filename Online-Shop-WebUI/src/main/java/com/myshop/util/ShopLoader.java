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
        // reset services (clear state)
        ctx.getUserService().clearServiceState();
        ctx.getProductService().clearServiceState();
        ctx.getOrderService().clearServiceState();

        // pre-load some extra products via productService (it wraps repo)
        // cast to impl to access helper addProduct (safe here)
        if (ctx.getProductService() instanceof ProductManagementServiceImpl) {
            ((ProductManagementServiceImpl) ctx.getProductService()).addProduct(new DefaultProduct(10, "Laptop", "Electronics", 55000));
            ((ProductManagementServiceImpl) ctx.getProductService()).addProduct(new DefaultProduct(11, "Shoes", "Fashion", 2500));
        }

        // create admin user via service
        ctx.getUserService().registerUser(new com.myshop.model.DefaultUser() {{
            setFirstName("Admin");
            setLastName("User");
            setEmail("admin@gmail.com");
            setPassword("admin123");
        }});

        // build Shop with services + context
        return new Shop(ctx.getUserService(), ctx.getProductService(), ctx.getOrderService(), ctx);
    }
}
