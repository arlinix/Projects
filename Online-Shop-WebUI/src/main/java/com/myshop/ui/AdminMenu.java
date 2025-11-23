
package com.myshop.ui;

import com.myshop.app.ApplicationContext;
import com.myshop.model.Role;
import com.myshop.service.AdminService;
import com.myshop.service.impl.AdminServiceImpl;

public class AdminMenu {
    private final ApplicationContext ctx;
    private final AdminService admin = new AdminServiceImpl();

    public AdminMenu(ApplicationContext ctx) { this.ctx = ctx; }

    public void start() {
        if (ctx.getLoggedInUser() == null || ctx.getLoggedInUser().getRole() != Role.ADMIN) {
            System.out.println("Access denied: Admins only.");
            return;
        }
        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Add Product");
            System.out.println("2. Edit Product");
            System.out.println("3. Delete Product");
            System.out.println("4. Process Order");
            System.out.println("5. Ship Order");
            System.out.println("6. Cancel Order");
            System.out.println("7. Reset User Email");
            System.out.println("8. Reset User Password");
            System.out.println("9. Back");
            System.out.print("Select: ");
            String in = ctx.getScanner().nextLine().trim();
            if ("9".equals(in)) return;
            try {
                switch (in) {
                    case "1" -> {
                        System.out.print("Name: "); String name = ctx.getScanner().nextLine().trim();
                        System.out.print("Category: "); String cat = ctx.getScanner().nextLine().trim();
                        System.out.print("Price: "); double price = Double.parseDouble(ctx.getScanner().nextLine().trim());
                        System.out.print("Stock: "); int stock = Integer.parseInt(ctx.getScanner().nextLine().trim());
                        int id = admin.createProduct(name, cat, price, stock);
                        System.out.println("Created product id=" + id);
                    }
                    case "2" -> {
                        System.out.print("ID: "); int id = Integer.parseInt(ctx.getScanner().nextLine().trim());
                        System.out.print("Name: "); String name = ctx.getScanner().nextLine().trim();
                        System.out.print("Category: "); String cat = ctx.getScanner().nextLine().trim();
                        System.out.print("Price: "); double price = Double.parseDouble(ctx.getScanner().nextLine().trim());
                        System.out.print("Stock: "); int stock = Integer.parseInt(ctx.getScanner().nextLine().trim());
                        System.out.print("Active (true/false): "); boolean active = Boolean.parseBoolean(ctx.getScanner().nextLine().trim());
                        System.out.println(admin.updateProduct(id, name, cat, price, stock, active) ? "Updated." : "Update failed.");
                    }
                    case "3" -> {
                        System.out.print("ID: "); int id = Integer.parseInt(ctx.getScanner().nextLine().trim());
                        System.out.println(admin.deleteProduct(id) ? "Deleted." : "Delete failed.");
                    }
                    case "4" -> {
                        System.out.print("Order ID: "); int oid = Integer.parseInt(ctx.getScanner().nextLine().trim());
                        System.out.println(admin.processOrder(oid, ctx.getLoggedInUser().getId()) ? "Order moved to PROCESSING." : "Fail.");
                    }
                    case "5" -> {
                        System.out.print("Order ID: "); int oid = Integer.parseInt(ctx.getScanner().nextLine().trim());
                        System.out.println(admin.shipOrder(oid, ctx.getLoggedInUser().getId()) ? "Order moved to SHIPPED." : "Fail.");
                    }
                    case "6" -> {
                        System.out.print("Order ID: "); int oid = Integer.parseInt(ctx.getScanner().nextLine().trim());
                        System.out.print("Reason: "); String reason = ctx.getScanner().nextLine().trim();
                        System.out.println(admin.cancelOrder(oid, ctx.getLoggedInUser().getId(), reason) ? "Order cancelled." : "Fail.");
                    }
                    case "7" -> {
                        System.out.print("User ID: "); int uid = Integer.parseInt(ctx.getScanner().nextLine().trim());
                        System.out.print("New Email: "); String email = ctx.getScanner().nextLine().trim();
                        System.out.println(admin.resetUserEmail(uid, email, ctx.getLoggedInUser().getId()) ? "Email reset." : "Fail.");
                    }
                    case "8" -> {
                        System.out.print("User ID: "); int uid = Integer.parseInt(ctx.getScanner().nextLine().trim());
                        System.out.print("Temp Password: "); String pw = ctx.getScanner().nextLine().trim();
                        System.out.println(admin.resetUserPassword(uid, pw, ctx.getLoggedInUser().getId()) ? "Password reset." : "Fail.");
                    }
                    default -> System.out.println("Unknown option.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
}
