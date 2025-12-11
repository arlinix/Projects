package com.myshop.controller;

import com.myshop.model.Product;
import com.myshop.service.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductManagementService productService;

    @Autowired
    public ProductController(ProductManagementService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(Model model) {
        Product[] products = productService.getProducts();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable int id, Model model) {
        Product p = productService.getProductById(id);
        model.addAttribute("product", p);
        return "product_details";
    }
}
