package com.example.demo.services;

import com.example.demo.models.Cart;
import com.example.demo.models.Consumer;
import com.example.demo.models.OrderLine;
import com.example.demo.repos.CartRepository;
import com.example.demo.repos.OrderLineRepository;
import com.example.demo.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private ConsumerService userService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    public boolean buy() {
        Consumer consumer = userService.getCurrentConsumer();
        if (consumer == null) {
            return false;
        }
        Cart currentCart = this.getCurrentCart(consumer);
        if (currentCart.getOrderLines().isEmpty()) {
            return false;
        }
        double sum = 0;
        for(OrderLine line : currentCart.getOrderLines()) {
            sum+=line.getProduct().getPrice();
        }
        //some business-logic
        currentCart.setActive(false);
        cartRepository.save(currentCart);
        return true;
    }

    public Cart getCurrentCart(Consumer consumer) {
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

    public boolean addToCart(long id, int count) {
        Consumer consumer = userService.getCurrentConsumer();
        if (consumer == null) {
            return false;
        }
        Cart cart = this.getCurrentCart(consumer);

        OrderLine orderLine = new OrderLine();
        orderLine.setCount(count);
        orderLine.setProduct(productRepository.getOne(id));
        orderLine.setCart(cart);

        orderLineRepository.save(orderLine);
        return true;
    }
}
