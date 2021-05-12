package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private ControllerUtils controllerUtils;

    @GetMapping("/")
    public String getMainPage(Model model) {

        //header
        controllerUtils.showHeader(model, null);

        return "main";
    }

}
