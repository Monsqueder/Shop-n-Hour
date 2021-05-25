package com.example.demo.controllers;

import com.example.demo.models.Cart;
import com.example.demo.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ControllerUtils controllerUtils;

    @GetMapping("/cart")
    public String getCart(Model model) {

        //header
        controllerUtils.showHeader(model, null);

        Cart cart = cartService.getCurrentCart();
        model.addAttribute("cart", cart);
        if (cart == null) {
            return "cart";
        }
        if (cart.getOrderLines() != null) {
            model.addAttribute("isEmpty", cart.getOrderLines().isEmpty());
        }
        return "cart";
    }

    @PostMapping("/cart/buy")
    public String buy(Model model, @RequestParam Date date){
        if (cartService.buy(date)){
            return "redirect:/";
        } else {
            return "redirect:/cart";
        }
    }
}
