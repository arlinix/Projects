package com.myshop.controller;

import com.myshop.service.ProductManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductManagementService productService;

    @Autowired
    public HomeController(ProductManagementService productService) {
        this.productService = productService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("products", productService.getProducts());
        return "index";
    }
}
