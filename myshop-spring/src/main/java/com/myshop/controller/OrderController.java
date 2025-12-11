package com.myshop.controller;

import com.myshop.model.User;
import com.myshop.service.OrderService;
import com.myshop.service.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService transactionalOrderService;
    private final OrderManagementService orderManagementService;

    @Autowired
    public OrderController(OrderService transactionalOrderService, OrderManagementService orderManagementService) {
        this.transactionalOrderService = transactionalOrderService;
        this.orderManagementService = orderManagementService;
    }

    @GetMapping("/checkout")
    public String checkoutPage() { return "checkout"; }

    @PostMapping("/place")
    public String placeOrder(String ccNumber, HttpSession session, Model model) {
        User u = (User) session.getAttribute("user");
        if (u == null) { model.addAttribute("error", "Please login before placing order"); return "login"; }
        try {
            int id = transactionalOrderService.placeOrder(u.getId(), ccNumber);
            model.addAttribute("success", "Order placed: " + id);
            return "orders";
        } catch (IllegalArgumentException iae) {
            model.addAttribute("error", iae.getMessage());
            return "checkout";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to place order: " + e.getMessage());
            return "checkout";
        }
    }

    @GetMapping("/my")
    public String myOrders(HttpSession session, Model model) {
        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/auth/login";
        model.addAttribute("orders", orderManagementService.getOrdersByUserId(u.getId()));
        return "orders";
    }
}
