package com.myshop.app;

import com.myshop.core.Shop;
import com.myshop.model.Cart;
import com.myshop.model.DefaultCart;
import com.myshop.model.User;
import com.myshop.repository.OrderRepository;
import com.myshop.repository.ProductRepository;
import com.myshop.repository.UserRepository;
import com.myshop.repository.impl.InMemoryOrderRepository;
import com.myshop.repository.impl.InMemoryProductRepository;
import com.myshop.repository.impl.InMemoryUserRepository;
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

    // repositories (created here, can be swapped later for DB repos)
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    // services (not singletons; created by context)
    private final UserManagementService userService;
    private final ProductManagementService productService;
    private final OrderManagementService orderService;

    private final Scanner scanner;

    // session + app wiring
    private Shop shop;
    private User loggedInUser;
    private Cart sessionCart;

    private ApplicationContext() {
        // create repositories
        this.userRepository = new InMemoryUserRepository();
        this.productRepository = new InMemoryProductRepository();
        this.orderRepository = new InMemoryOrderRepository();

        // create services (inject the repo implementations)
        this.userService = new UserManagementServiceImpl(userRepository);
        this.productService = new ProductManagementServiceImpl(productRepository);
        this.orderService = new OrderManagementServiceImpl(orderRepository);

        // scanner & session
        this.scanner = new Scanner(System.in);
        this.sessionCart = new DefaultCart();
        this.loggedInUser = null;

        // create shop via loader (pass context) to avoid circular access
        this.shop = ShopLoader.loadShop(this);
    }

    public static synchronized ApplicationContext getInstance() {
        if (instance == null) instance = new ApplicationContext();
        return instance;
    }

    // --- getters for core components
    public UserManagementService getUserService() { return userService; }
    public ProductManagementService getProductService() { return productService; }
    public OrderManagementService getOrderService() { return orderService; }

    public Shop getShop() { return shop; }
    public Scanner getScanner() { return scanner; }

    // session API
    public User getLoggedInUser() { return loggedInUser; }

    public void setLoggedInUser(User user) {
        if (user != null && this.sessionCart != null) this.sessionCart.clear();
        this.loggedInUser = user;
    }

    public Cart getSessionCart() { return sessionCart; }
    public void resetSessionCart() { this.sessionCart = new DefaultCart(); }

    // test / reset
    public void clearApplicationState() {
        userService.clearServiceState();
        productService.clearServiceState();
        orderService.clearServiceState();
        if (shop != null) shop.clearState();
        this.loggedInUser = null;
        this.sessionCart = new DefaultCart();
    }
}
