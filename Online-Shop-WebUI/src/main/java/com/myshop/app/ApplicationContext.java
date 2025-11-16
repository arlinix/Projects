package com.myshop.app;

import com.myshop.core.Shop;
import com.myshop.model.Cart;
import com.myshop.model.DefaultCart;
import com.myshop.model.User;
import com.myshop.service.OrderManagementService;
import com.myshop.service.ProductManagementService;
import com.myshop.service.UserManagementService;
import com.myshop.service.impl.OrderManagementServiceImpl;
import com.myshop.service.impl.ProductManagementServiceImpl;
import com.myshop.service.impl.UserManagementServiceImpl;
import com.myshop.util.ShopLoader;

import java.util.Scanner;

public class ApplicationContext {

    private static ApplicationContext instance;

    private final UserManagementService userService;
    private final ProductManagementService productService;
    private final OrderManagementService orderService;
    private final Scanner scanner;

    // created after services and session exists
    private Shop shop;

    // session
    private User loggedInUser;
    private Cart sessionCart;

    private ApplicationContext() {
        // initialize services first (singletons)
        this.userService = UserManagementServiceImpl.getInstance();
        this.productService = ProductManagementServiceImpl.getInstance();
        this.orderService = OrderManagementServiceImpl.getInstance();

        //scanner + session objects
        this.scanner = new Scanner(System.in);
        this.sessionCart = new DefaultCart();
        this.loggedInUser = null;

        //create shop via loader (pass 'this' to avoid recursion)
        this.shop = ShopLoader.loadShop(this);
    }

    public static synchronized ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public UserManagementService getUserService() {
        return userService;
    }

    public ProductManagementService getProductService() {
        return productService;
    }

    public OrderManagementService getOrderService() {
        return orderService;
    }

    public Shop getShop() {
        return shop;
    }

    public Scanner getScanner() {
        return scanner;
    }


    // session API
    public User getLoggedInUser() {
        return loggedInUser;
    }

    //when new user logs in, clear previous cart per spec
    public void setLoggedInUser(User user) {
        if (user != null && this.sessionCart != null) this.sessionCart.clear();
        this.loggedInUser = user;
    }

    public Cart getSessionCart() {
        return sessionCart;
    }

    public void resetSessionCart() {
        this.sessionCart = new DefaultCart();
    }

    public void clearApplicationState() {
        userService.clearServiceState();
        productService.clearServiceState();
        orderService.clearServiceState();
        if (shop != null) shop.clearState();
        this.loggedInUser = null;
        this.sessionCart = new DefaultCart();
    }


}