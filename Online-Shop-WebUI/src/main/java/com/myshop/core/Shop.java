package com.myshop.core;

import com.myshop.app.ApplicationContext;
import com.myshop.model.Cart;
import com.myshop.model.DefaultOrder;
import com.myshop.model.Product;
import com.myshop.model.User;
import com.myshop.service.OrderManagementService;
import com.myshop.service.ProductManagementService;
import com.myshop.service.UserManagementService;

public class Shop {

    private final UserManagementService userService;
    private final ProductManagementService productService;
    private final OrderManagementService orderService;
    private final ApplicationContext context;

    public Shop(UserManagementService userService,
                ProductManagementService productService,
                OrderManagementService orderService,
                ApplicationContext context) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.context = context;
    }

    // registration
    public String register(String firstName, String lastName, String email, String password) {
        User user = new com.myshop.model.DefaultUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        return userService.registerUser(user);
    }

    // login
    public boolean login(String email, String password) {
        User user = userService.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            context.setLoggedInUser(user);
            return true;
        }
        return false;
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
        Cart cart = context.getSessionCart();
        cart.addProduct(product);
        return "Product " + product.getProductName() + " has been added to your cart. If you want to add a new product - enter the product id. If you want to proceed with checkout - enter word 'checkout' to console";
    }

    // place order
    public String placeOrder(String creditCardNumber) {
        Cart cart = context.getSessionCart();
        if (cart.isEmpty()) return "Your cart is empty. Please, add product to cart first and then proceed with checkout";
        if (context.getLoggedInUser() == null) return "You are not logged in. Please, sign in or create new account";

        DefaultOrder order = new DefaultOrder();
        if (!order.isCreditCardNumberValid(creditCardNumber)) return "You entered invalid credit card number. Valid credit card should contain 16 digits. Please, try one more time.";
        order.setCreditCardNumber(creditCardNumber);
        order.setCustomerId(context.getLoggedInUser().getId());
        order.setProducts(cart.getProducts());
        orderService.addOrder(order);
        cart.clear();
        return "Thanks a lot for your purchase. Details about order delivery are sent to your email.";
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
        return true;
    }

    public boolean changeEmail(String newEmail) {
        User user = context.getLoggedInUser();
        if (user == null) return false;
        user.setEmail(newEmail);
        return true;
    }

    // clear state helper
    public void clearState() {
        userService.clearServiceState();
        productService.clearServiceState();
        orderService.clearServiceState();
        com.myshop.model.DefaultUser.clearState();
        if (context.getSessionCart() != null) context.getSessionCart().clear();
        context.setLoggedInUser(null);
    }
}
