package com.myshop.app;

import com.myshop.service.impl.UserManagementServiceImpl;
import com.myshop.service.impl.ProductManagementServiceImpl;
import com.myshop.service.impl.OrderManagementServiceImpl;
import com.myshop.core.Shop;
import com.myshop.util.ShopLoader;

import java.util.Scanner;

public class ApplicationContext {

    private static ApplicationContext instance;

    private final UserManagementServiceImpl userService;
    private final ProductManagementServiceImpl productService;
    private final OrderManagementServiceImpl orderService;
    private final Shop shop;
    private final Scanner scanner;

    private ApplicationContext() {
        this.userService = UserManagementServiceImpl.getInstance();
        this.productService = ProductManagementServiceImpl.getInstance();
        this.orderService = OrderManagementServiceImpl.getInstance();
        this.shop = ShopLoader.loadShop();
        this.scanner = new Scanner(System.in);
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public UserManagementServiceImpl getUserService() {
        return userService;
    }

    public ProductManagementServiceImpl getProductService() {
        return productService;
    }

    public OrderManagementServiceImpl getOrderService() {
        return orderService;
    }

    public Shop getShop() {
        return shop;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void clearApplicationState() {
        userService.clearServiceState();
        productService.clearServiceState();
        orderService.clearServiceState();
        shop.clearShopState();
    }
}