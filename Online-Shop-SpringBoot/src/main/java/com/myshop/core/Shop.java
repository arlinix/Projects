package com.myshop.core;

import com.myshop.app.ApplicationContext;
import com.myshop.db.DbConnectionFactory;
import com.myshop.model.Cart;
import com.myshop.model.DefaultOrder;
import com.myshop.model.DefaultProduct;
import com.myshop.model.Product;
import com.myshop.model.User;
import com.myshop.repository.CartRepository;
import com.myshop.repository.ProductRepository;
import com.myshop.repository.UserRepository;
import com.myshop.repository.jdbc.CartRepositoryJdbcImpl;
import com.myshop.repository.jdbc.ProductRepositoryJdbcImpl;
import com.myshop.repository.jdbc.UserRepositoryJdbcImpl;
import com.myshop.service.OrderManagementService;
import com.myshop.service.ProductManagementService;
import com.myshop.service.UserManagementService;
import com.myshop.service.OrderService;

import java.sql.Connection;

public class Shop {

    private final UserManagementService userService;
    private final ProductManagementService productService;
    private final OrderManagementService orderService;
    private final OrderService transactionalOrderService;
    private final ApplicationContext context;

    public Shop(UserManagementService userService,
                ProductManagementService productService,
                OrderManagementService orderService,
                OrderService transactionalOrderService,
                ApplicationContext context) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.transactionalOrderService = transactionalOrderService;
        this.context = context;
    }

    // registration
    public String register(String firstName, String lastName, String email, String password) {
        User user = new com.myshop.model.DefaultUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        try {
            return userService.registerUser(user);
        } catch (Exception e) {
            return "Failed to register user: " + e.getMessage();
        }
    }

    // login
    public boolean login(String email, String password) {
        try {
            User user = userService.getUserByEmail(email);
            if (user != null && user.getPassword().equals(password)) {
                context.setLoggedInUser(user);
                // if user logs in, migrate session cart items into DB (optional)
                migrateSessionCartToDb(user.getId());
                return true;
            }
        } catch (Exception e) {
            // log
        }
        return false;
    }

    private void migrateSessionCartToDb(int userId) {
        try (Connection conn = DbConnectionFactory.getConnection()) {
            CartRepository cartRepo = new CartRepositoryJdbcImpl();
            Cart session = context.getSessionCart();
            if (session == null) return;
            for (Product p : session.getProducts()) {
                cartRepo.addItem(conn, userId, p.getId(), 1);
            }
            session.clear();
        } catch (Exception e) {
            // ignore migration failure
        }
    }

    // logout
    public void logout() { context.setLoggedInUser(null); }

    // products
    public Product[] getAllProducts() { return productService.getProducts(); }
    public Product getProductById(int id) { return productService.getProductById(id); }

    // cart
    public String addProductToCart(int productId) {
        Product product = productService.getProductById(productId);
        if (product == null) return "Please, enter product ID if you want to add product to cart. Or enter 'checkout' if you want to proceed with checkout. Or enter 'menu' if you want to navigate back to the main menu.";
        if (context.getLoggedInUser() == null) {
            // anonymous session cart
            Cart cart = context.getSessionCart();
            cart.addProduct(product);
            return "Product " + product.getProductName() + " has been added to your cart (session).";
        } else {
            // persist to DB
            try (Connection conn = DbConnectionFactory.getConnection()) {
                CartRepository cartRepo = new com.myshop.repository.jdbc.CartRepositoryJdbcImpl();
                cartRepo.addItem(conn, context.getLoggedInUser().getId(), productId, 1);
                return "Product " + product.getProductName() + " has been added to your cart.";
            } catch (Exception e) {
                return "Failed to add product to cart: " + e.getMessage();
            }
        }
    }

    // place order
    public String placeOrder(String creditCardNumber) {
        if (context.getLoggedInUser() == null) return "You are not logged in. Please, sign in or create new account";
        try {
            int orderId = transactionalOrderService.placeOrder(context.getLoggedInUser().getId(), creditCardNumber);
            return "Thanks a lot for your purchase. Order id: " + orderId;
        } catch (IllegalArgumentException iae) {
            return iae.getMessage();
        } catch (Exception e) {
            return "Failed to place order: " + e.getMessage();
        }
    }

    public com.myshop.model.Order[] getMyOrders() {
        if (context.getLoggedInUser() == null) return null;
        return orderService.getOrdersByUserId(context.getLoggedInUser().getId());
    }

    // settings
    public boolean changePassword(String newPassword) {
        User user = context.getLoggedInUser();
        if (user == null) return false;
        user.setPassword(newPassword);
        try (Connection conn = DbConnectionFactory.getConnection()) {
            UserRepository ur = new UserRepositoryJdbcImpl();
            return ur.update(conn, (com.myshop.model.DefaultUser) user);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean changeEmail(String newEmail) {
        User user = context.getLoggedInUser();
        if (user == null) return false;
        user.setEmail(newEmail);
        try (Connection conn = DbConnectionFactory.getConnection()) {
            UserRepository ur = new UserRepositoryJdbcImpl();
            return ur.update(conn, (com.myshop.model.DefaultUser) user);
        } catch (Exception e) {
            return false;
        }
    }

    // clear state helper
    public void clearState() {
        try {
            userService.clearServiceState();
            productService.clearServiceState();
            orderService.clearServiceState();
            com.myshop.model.DefaultUser.clearState();
            if (context.getSessionCart() != null) context.getSessionCart().clear();
            context.setLoggedInUser(null);
        } catch (Exception e) {
            // ignore
        }
    }
}