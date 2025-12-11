package com.myshop.controller;

import com.myshop.model.Product;
import com.myshop.service.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductManagementService productService;

    @Autowired
    public CartController(ProductManagementService productService) {
        this.productService = productService;
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable int id, HttpSession session) {
        var cart = session.getAttribute("cart");
        ArrayList<Product> list;
        if (cart == null) { list = new ArrayList<>(); session.setAttribute("cart", list);} else list = (ArrayList<Product>) cart;
        Product p = productService.getProductById(id);
        if (p != null) list.add(p);
        return "redirect:/cart/view";
    }

    @GetMapping("/view")
    public String viewCart(Model model, HttpSession session) {
        var cart = session.getAttribute("cart");
        model.addAttribute("cartItems", cart == null ? java.util.Collections.emptyList() : cart);
        return "cart";
    }
}
