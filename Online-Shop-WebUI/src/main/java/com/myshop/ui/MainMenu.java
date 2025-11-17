package com.myshop.ui;

import com.myshop.app.ApplicationContext;
import com.myshop.core.Shop;
import com.myshop.model.Product;
import com.myshop.model.User;

import java.util.Scanner;

public class MainMenu {

    private final ApplicationContext ctx = ApplicationContext.getInstance();
    private final Shop shop = ctx.getShop();
    private final Scanner scanner = ctx.getScanner();

    public MainMenu() throws Exception {
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String input = scanner.nextLine().trim();
            if ("exit".equalsIgnoreCase(input)) {
                System.out.println("Exiting program. Bye!");
                running = false;
                continue;
            }
            switch (input) {
                case "1": cmdSignUp(); break;
                case "2": cmdSignInOrOut(); break;
                case "3": cmdProductCatalog(); break;
                case "4": cmdMyOrders(); break;
                case "5": cmdSettings(); break;
                case "6": cmdCustomerList(); break;
                default:
                    System.out.println("Only 1, 2, 3, 4, 5 is allowed. Try one more time.");
            }
        }
    }

    private void printMainMenu() {
        User logged = ctx.getLoggedInUser();
        String signLabel = (logged == null) ? "Sign In" : "Sign Out";
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Sign Up");
        System.out.println("2. " + signLabel);
        System.out.println("3. Product Catalog");
        System.out.println("4. My Orders");
        System.out.println("5. Settings");
        System.out.println("6. Customer List");
        System.out.println("Type 'exit' to close program.");
        System.out.print("Select option: ");
    }

    private void cmdSignUp() {
        System.out.println("\n--- Sign Up ---");
        System.out.print("Enter first name: ");
        String first = scanner.nextLine().trim();
        System.out.print("Enter last name: ");
        String last = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String pass = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        String res = shop.register(first, last, email, pass);
        System.out.println(res);
        if ("New user is created".equals(res)) {
            shop.login(email, pass);
            System.out.println("You are now signed in.");
        }
    }

    private void cmdSignInOrOut() {
        if (ctx.getLoggedInUser() == null) {
            System.out.println("\n--- Sign In ---");
            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Enter password: ");
            String pass = scanner.nextLine().trim();
            boolean ok = shop.login(email, pass);
            if (ok) {
                System.out.println("Glad to see you back " + ctx.getLoggedInUser().getFirstName() + " " + ctx.getLoggedInUser().getLastName());
            } else {
                System.out.println("Unfortunately, such login and password doesn’t exist");
            }
        } else {
            System.out.println("Have a nice day! Look forward to welcoming back!");
            shop.logout();
        }
    }

    private void cmdProductCatalog() {
        while (true) {
            System.out.println("\n--- Product Catalog ---");
            Product[] products = shop.getAllProducts();
            for (Product p : products) {
                System.out.printf("%d) %s (%s) - %.2f%n", p.getId(), p.getProductName(), p.getCategoryName(), p.getPrice());
            }
            System.out.println("Enter product id to add it to the cart or 'checkout' to proceed to checkout or 'menu' to go back to main menu.");
            System.out.print("Your input: ");
            String input = scanner.nextLine().trim();
            if ("menu".equalsIgnoreCase(input)) return;
            if ("checkout".equalsIgnoreCase(input)) {
                if (ctx.getSessionCart().isEmpty() && ctx.getLoggedInUser() == null) {
                    System.out.println("Your cart is empty. Please, add product to cart first and then proceed with checkout");
                    continue;
                }
                if (ctx.getLoggedInUser() == null) {
                    System.out.println("You are not logged in. Please, sign in or create new account");
                    return;
                }
                while (true) {
                    System.out.print("Enter your credit card number without spaces and press enter if you confirm purchase: ");
                    String cc = scanner.nextLine().trim();
                    String msg = shop.placeOrder(cc);
                    if (msg.startsWith("Thanks a lot")) {
                        System.out.println(msg);
                        return;
                    } else {
                        System.out.println(msg);
                    }
                }
            }
            try {
                int id = Integer.parseInt(input);
                String msg = shop.addProductToCart(id);
                System.out.println(msg);
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter a valid product id, 'checkout' or 'menu'.");
            }
        }
    }

    private void cmdMyOrders() {
        System.out.println("\n--- My Orders ---");
        if (ctx.getLoggedInUser() == null) {
            System.out.println("Please, log in or create new account to see list of your orders");
            return;
        }
        var orders = shop.getMyOrders();
        if (orders == null || orders.length == 0) {
            System.out.println("Unfortunately, you don’t have any orders yet. Navigate back to main menu to place a new order");
            return;
        }
        for (var o : orders) System.out.println(o);
    }

    private void cmdSettings() {
        System.out.println("\n--- Settings ---");
        if (ctx.getLoggedInUser() == null) {
            System.out.println("Please, log in or create new account to change your account settings");
            return;
        }
        while (true) {
            System.out.println("1. Change Password");
            System.out.println("2. Change Email");
            System.out.println("Type 'menu' to go back to main menu");
            System.out.print("Select option: ");
            String opt = scanner.nextLine().trim();
            if ("menu".equalsIgnoreCase(opt)) return;
            if ("1".equals(opt)) {
                System.out.print("Enter new password: ");
                String np = scanner.nextLine().trim();
                if (shop.changePassword(np)) System.out.println("Your password has been successfully changed");
                return;
            } else if ("2".equals(opt)) {
                System.out.print("Enter new email: ");
                String ne = scanner.nextLine().trim();
                if (shop.changeEmail(ne)) System.out.println("Your email has been successfully changed");
                return;
            } else {
                System.out.println("Only 1, 2 is allowed. Try one more time");
            }
        }
    }

    private void cmdCustomerList() {
        System.out.println("\n--- Customer List ---");
        try {
            var users = ctx.getUserService().getUsers();
            for (User u : users) {
                System.out.printf("ID:%d Name:%s %s Email:%s%n", u.getId(), u.getFirstName(), u.getLastName(), u.getEmail());
            }
        } catch (Exception e) {
            System.out.println("Failed to load users: " + e.getMessage());
        }
    }
}
