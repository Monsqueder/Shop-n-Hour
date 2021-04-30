package com.example.demo.controllers;

import com.example.demo.models.Consumer;
import com.example.demo.repos.ConsumerRepository;
import com.example.demo.services.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SignInSignUpController {

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ConsumerService userDetailsService;

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
            Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(Collectors.toMap(
                    fieldError -> fieldError.getField() + "Error",
                    FieldError::getDefaultMessage));
            model.mergeAttributes(errors);
            return "signup";
        } else if (!consumer.getPasswordConfirm().equals(consumer.getPassword())) {
            model.addAttribute("passwordsError", "Passwords are not identical");
            return "signup";
        } else {
            model.addAttribute("message", null);
            if (!userDetailsService.addUser(consumer)) {
                return "signup";
            }
            return "redirect:/signin";
        }
    }

    @GetMapping("/activate/{code}")
    public String activateConsumer(Model model, @PathVariable String code) {
        boolean isActivated = userDetailsService.activateConsumer(code);
        if (isActivated) {
            model.addAttribute("message", "User was activated successfully!");
        } else {
            model.addAttribute("message", "Failed activation!");
        }
        return "signin";
    }
}
