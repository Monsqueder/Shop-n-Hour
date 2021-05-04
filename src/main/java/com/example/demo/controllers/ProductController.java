package com.example.demo.controllers;

import com.example.demo.models.Comment;
import com.example.demo.models.Product;
import com.example.demo.repos.ProductRepository;
import com.example.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public String getProductPage(Model model, @PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return "redirect:/";
        }

        Product product = productRepository.getOne(id);

        model.addAttribute("product", product);
        model.addAttribute("comments", product.getComments());
        return "product";
    }

    @PostMapping("/addToCart/{id}")
    public String addToCart(@PathVariable Long id, @RequestParam int count) {
        if (productService.addToCart(id, count)) {
            return "redirect:/";
        } else {
            return "redirect:/product/{id}";
        }
    }

    @PostMapping("/addComment/{id}")
    public String addComment(Model model, @PathVariable Long id, @ModelAttribute Comment comment) {
        if (productService.addComment(id, comment)) {
            model.addAttribute("message", "Added comment!");
        } else {
            model.addAttribute("message", "Failed!");
        }
        return "redirect:/product/{id}";
    }

}
