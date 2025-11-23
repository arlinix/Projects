
package com.myshop.util;

import com.myshop.app.ApplicationContext;
import com.myshop.core.Shop;
import com.myshop.model.DefaultProduct;
import com.myshop.model.Role;

public class ShopLoader {
    private ShopLoader() {}

    public static Shop loadShop(ApplicationContext ctx) {
        try {
            ctx.getUserService().clearServiceState();
            ctx.getProductService().clearServiceState();
            ctx.getOrderService().clearServiceState();
        } catch (Exception e) { /* ignore */ }

        // REMOVE: the unconditional adds of Laptop/Shoes.
        // Rely on ProductManagementServiceImpl#initDefaults() which seeds only when empty.

        // Seed Admin user with ADMIN role (registerUser will ignore if email exists)
        try {
            ctx.getUserService().registerUser(new com.myshop.model.DefaultUser() {{
                setFirstName("Admin");
                setLastName("User");
                setEmail("admin@gmail.com");
                setPassword("admin123");
                setRole(Role.ADMIN);     // NEW: assign admin role
            }});
        } catch (Exception e) { /* ignore */ }

        return new Shop(ctx.getUserService(), ctx.getProductService(), ctx.getOrderService(), new com.myshop.service.OrderService(), ctx);
    }
}
