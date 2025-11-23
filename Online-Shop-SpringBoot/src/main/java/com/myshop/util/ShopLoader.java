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
        try {
            ctx.getUserService().clearServiceState();
            ctx.getProductService().clearServiceState();
            ctx.getOrderService().clearServiceState();
        } catch (Exception e) {
            // ignore
        }

        if (ctx.getProductService() instanceof ProductManagementServiceImpl) {
            try {
                ((ProductManagementServiceImpl) ctx.getProductService()).addProduct(new DefaultProduct(10, "Laptop", "Electronics", 55000));
                ((ProductManagementServiceImpl) ctx.getProductService()).addProduct(new DefaultProduct(11, "Shoes", "Fashion", 2500));
            } catch (Exception e) {
                // ignore
            }
        }

        try {
            ctx.getUserService().registerUser(new com.myshop.model.DefaultUser() {{
                setFirstName("Admin");
                setLastName("User");
                setEmail("admin@gmail.com");
                setPassword("admin123");
            }});
        } catch (Exception e) {
            // ignore
        }

        // create Shop with services + context
        return new Shop(ctx.getUserService(), ctx.getProductService(), ctx.getOrderService(), new com.myshop.service.OrderService(), ctx);
    }
}
