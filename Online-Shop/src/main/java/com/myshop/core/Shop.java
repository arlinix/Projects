package com.myshop.core;

import com.myshop.app.ApplicationContext;
import com.myshop.model.DefaultOrder;
import com.myshop.model.DefaultUser;
import com.myshop.model.Product;
import com.myshop.service.OrderManagementService;
import com.myshop.service.ProductManagementService;
import com.myshop.service.UserManagementService;
import com.myshop.model.Cart;

public class Shop {

    private final UserManagementService userService;
    private final ProductManagementService productService;
    private final OrderManagementService orderService;
    private final ApplicationContext context = ApplicationContext.getInstance();

    public Shop(UserManagementService userService,
                ProductManagementService productService,
                OrderManagementService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
    }

    // ========================= USER REGISTRATION =========================
    public String register(String firstName, String lastName, String email, String password) {
        DefaultUser user = new DefaultUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);

        return userService.registerUser(user);
    }

    // ========================= LOGIN =========================
    public boolean login(String email, String password) {
        var user = userService.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            context.setLoggedInUser(user);
            return true;
        }
        return false;
    }

    // ========================= LOGOUT =========================
    public void logout() {
        context.setLoggedInUser(null);
    }

    // ========================= PRODUCT CATALOG =========================
    public Product[] getAllProducts() {
        return productService.getProducts();
    }

    public Product getProductById(int id) {
        return productService.getProductById(id);
    }

    // ========================= ADD TO CART =========================
    public String addProductToCart(int productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return "Invalid product ID";
        }
        Cart cart = context.getSessionCart();
        cart.addProduct(product);
        return "Product " + product.getProductName() + " has been added to your cart.";
    }

    // ========================= PLACE ORDER =========================
    public String placeOrder(String creditCardNumber) {
        Cart cart = context.getSessionCart();
        if (cart.isEmpty()) {
            return "Your cart is empty. Please add products first.";
        }

        if (context.getLoggedInUser() == null) {
            return "You are not logged in. Please sign in first.";
        }

        DefaultOrder order = new DefaultOrder();

        if (!order.isCreditCardNumberValid(creditCardNumber)) {
            return "Invalid credit card number. It must contain exactly 16 digits.";
        }

        order.setCreditCardNumber(creditCardNumber);
        order.setCustomerId(context.getLoggedInUser().getId());
        order.setProducts(cart.getProducts());

        orderService.addOrder(order);
        cart.clear();

        return "Thanks for your purchase. Order confirmation sent to your email.";
    }

    // ========================= MY ORDERS =========================
    public com.myshop.model.Order[] getMyOrders() {
        if (context.getLoggedInUser() == null) {
            return null;
        }
        return orderService.getOrdersByUserId(context.getLoggedInUser().getId());
    }

    // ========================= SETTINGS =========================
    public boolean changePassword(String newPassword) {
        var user = context.getLoggedInUser();
        if (user == null) return false;
        user.setPassword(newPassword);
        return true;
    }

    public boolean changeEmail(String newEmail) {
        var user = context.getLoggedInUser();
        if (user == null) return false;
        user.setEmail(newEmail);
        return true;
    }

    // ========================= TEST SUPPORT =========================
    public void clearState() {
        userService.clearServiceState();
        productService.clearServiceState();
        orderService.clearServiceState();
        DefaultUser.clearState();
        if (context.getSessionCart() != null) {
            context.getSessionCart().clear();
        }
        context.setLoggedInUser(null);
    }
}
