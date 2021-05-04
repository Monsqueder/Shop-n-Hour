package com.example.demo.services;

import com.example.demo.models.Cart;
import com.example.demo.models.Consumer;
import com.example.demo.models.OrderLine;
import com.example.demo.repos.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private ConsumerService userService;

    @Autowired
    private CartRepository cartRepository;

    public boolean buy() {
        Cart cart = this.getCurrentCart();
        if (cart == null) {
            return false;
        }
        if (cart.getOrderLines().isEmpty()) {
            return false;
        }
        double sum = 0;
        for(OrderLine line : cart.getOrderLines()) {
            sum+=line.getProduct().getPrice();
        }
        //some business-logic
        cart.setActive(false);
        cartRepository.save(cart);
        return true;
    }

    public Cart getCurrentCart() {
        Consumer consumer = userService.getCurrentConsumer();
        if (consumer == null) {
            return null;
        }
        for(Cart cart : consumer.getCarts()) {
            if(cart.isActive()){
                return cart;
            }
        }
        Cart cart = new Cart();
        cart.setConsumer(consumer);
        cart.setActive(true);
        cartRepository.save(cart);
        return cart;
    }
}
