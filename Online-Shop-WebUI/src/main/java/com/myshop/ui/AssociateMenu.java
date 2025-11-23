
package com.myshop.ui;

import com.myshop.app.ApplicationContext;
import com.myshop.model.Role;
import com.myshop.service.AssociateService;
import com.myshop.service.impl.AssociateServiceImpl;

public class AssociateMenu {
    private final ApplicationContext ctx;
    private final AssociateService svc = new AssociateServiceImpl();

    public AssociateMenu(ApplicationContext ctx) { this.ctx = ctx; }

    public void start() {
        if (ctx.getLoggedInUser() == null || ctx.getLoggedInUser().getRole() == Role.CUSTOMER) {
            System.out.println("Access denied: Associates/Admins only.");
            return;
        }
        while (true) {
            System.out.println("\n=== ASSOCIATE MENU ===");
            System.out.println("1. View All Orders");
            System.out.println("2. Process Order");
            System.out.println("3. Cancel Order");
            System.out.println("4. Back");
            System.out.print("Select: ");
            String in = ctx.getScanner().nextLine().trim();
            if ("4".equals(in)) return;
            try {
                switch (in) {
                    case "1" -> {
                        var orders = svc.listAllOrders();
                        if (orders.length == 0) System.out.println("No orders.");
                        for (var o : orders) System.out.println(o);
                    }
                    case "2" -> {
                        System.out.print("Order ID: "); int oid = Integer.parseInt(ctx.getScanner().nextLine().trim());
                        System.out.println(svc.processOrder(oid, ctx.getLoggedInUser().getId()) ? "Order moved to PROCESSING." : "Fail.");
                    }
                    case "3" -> {
                        System.out.print("Order ID: "); int oid = Integer.parseInt(ctx.getScanner().nextLine().trim());
                        System.out.print("Reason: "); String reason = ctx.getScanner().nextLine().trim();
                        System.out.println(svc.cancelOrder(oid, ctx.getLoggedInUser().getId(), reason) ? "Order cancelled." : "Fail.");
                    }
                    default -> System.out.println("Unknown option.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }
}
