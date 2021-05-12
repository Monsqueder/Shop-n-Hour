package com.example.demo.controllers;

import com.example.demo.models.Product;
import com.example.demo.services.ModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminModeratorController {

    @Autowired
    private ModeratorService moderatorService;

    @Autowired
    private ControllerUtils controllerUtils;

    @GetMapping("/moderator")
    public String getProductPage(Model model) {

        //header
        controllerUtils.showHeader(model, null);

        model.addAttribute("product", new Product());
        return "moderator";
    }

    @PostMapping("/moderator/addProduct")
    public String addProduct(Model model, @ModelAttribute Product product, @RequestParam("file") MultipartFile file){
        if (moderatorService.addProduct(product, file)) {
            model.addAttribute("message", "Added successfully!");
        } else {
            model.addAttribute("message", "Failed!");
        }
        return "moderator";
    }

}
