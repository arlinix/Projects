package com.myshop.app;

import com.myshop.core.Shop;
import com.myshop.model.Cart;
import com.myshop.model.DefaultCart;
import com.myshop.model.User;
import com.myshop.repository.OrderRepository;
import com.myshop.repository.ProductRepository;
import com.myshop.repository.UserRepository;
import com.myshop.repository.jdbc.OrderRepositoryJdbcImpl;
import com.myshop.repository.jdbc.ProductRepositoryJdbcImpl;
import com.myshop.repository.jdbc.UserRepositoryJdbcImpl;
import com.myshop.service.OrderManagementService;
import com.myshop.service.ProductManagementService;
import com.myshop.service.UserManagementService;
import com.myshop.service.impl.OrderManagementServiceImpl;
import com.myshop.service.impl.ProductManagementServiceImpl;
import com.myshop.service.impl.UserManagementServiceImpl;
import com.myshop.service.OrderService;
import com.myshop.util.ShopLoader;

import java.util.Scanner;

public class ApplicationContext {

    private static ApplicationContext instance;

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final UserManagementService userService;
    private final ProductManagementService productService;
    private final OrderManagementService orderService;

    private final Scanner scanner;

    private Shop shop;
    private User loggedInUser;
    private Cart sessionCart;

    private ApplicationContext() throws Exception {
        this.userRepository = new UserRepositoryJdbcImpl();
        this.productRepository = new ProductRepositoryJdbcImpl();
        this.orderRepository = new OrderRepositoryJdbcImpl();

        this.userService = new UserManagementServiceImpl(userRepository);
        this.productService = new ProductManagementServiceImpl(productRepository);
        this.orderService = new OrderManagementServiceImpl(orderRepository);

        this.scanner = new Scanner(System.in);
        this.sessionCart = new DefaultCart();
        this.loggedInUser = null;

        this.shop = ShopLoader.loadShop(this);
    }

    public static synchronized ApplicationContext getInstance() throws Exception {
        if (instance == null) instance = new ApplicationContext();
        return instance;
    }

    public UserManagementService getUserService() { return userService; }
    public ProductManagementService getProductService() { return productService; }
    public OrderManagementService getOrderService() { return orderService; }

    public Shop getShop() { return shop; }
    public Scanner getScanner() { return scanner; }

    public User getLoggedInUser() { return loggedInUser; }

    public void setLoggedInUser(User user) {
        if (user != null && this.sessionCart != null) this.sessionCart.clear();
        this.loggedInUser = user;
    }

    public Cart getSessionCart() { return sessionCart; }
    public void resetSessionCart() { this.sessionCart = new DefaultCart(); }

    public void clearApplicationState() {
        try {
            userService.clearServiceState();
            productService.clearServiceState();
            orderService.clearServiceState();
        } catch (Exception e) {
            // ignore
        }
        if (shop != null) shop.clearState();
        this.loggedInUser = null;
        this.sessionCart = new DefaultCart();
    }
}