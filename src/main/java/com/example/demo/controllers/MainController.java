package com.example.demo.controllers;

import com.example.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String getMainPage(Model model) {

        //header
        controllerUtils.showHeader(model, null);
        model.addAttribute("productLine1", productService.getProductLine(1, 6));
        model.addAttribute("productLine2", productService.getProductLine(2, 6));

        return "main";
    }

}
