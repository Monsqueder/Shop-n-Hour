package com.example.demo.controllers;

import com.example.demo.models.Cart;
import com.example.demo.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public String getCart(Model model) {
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
    public String buy(Model model){
        if (cartService.buy()){
            return "redirect:/";
        } else {
            return "redirect:/cart";
        }
    }
}
