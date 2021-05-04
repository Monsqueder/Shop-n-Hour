package com.example.demo.controllers;

import com.example.demo.models.Consumer;
import com.example.demo.services.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class SignInSignUpController {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private ControllerUtils utils;

    @GetMapping("/signin")
    public String getSignInPage() {
        return "signin";
    }

    @GetMapping("/signup")
    public String getSignUpPage(Model model) {
        model.addAttribute("consumer", new Consumer());
        return "signup";
    }

    @PostMapping("/signup")
    public String createConsumer(@ModelAttribute @Valid Consumer consumer, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            utils.showErrors(model, bindingResult);
            return "signup";
        } else if (!consumer.getPasswordConfirm().equals(consumer.getPassword())) {
            model.addAttribute("passwordsError", "Passwords are not identical");
            return "signup";
        } else {
            model.addAttribute("message", null);
            if (!consumerService.addUser(consumer)) {
                return "signup";
            }
            return "redirect:/signin";
        }
    }

    @GetMapping("/activate/{code}")
    public String activateConsumer(Model model, @PathVariable String code) {
        boolean isActivated = consumerService.activateConsumer(code);
        if (isActivated) {
            model.addAttribute("message", "User was activated successfully!");
        } else {
            model.addAttribute("message", "Failed activation!");
        }
        return "signin";
    }
}
