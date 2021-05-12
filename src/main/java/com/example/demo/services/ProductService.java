package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.repos.CommentRepository;
import com.example.demo.repos.OrderLineRepository;
import com.example.demo.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ConsumerService consumerService;

    public boolean addToCart(long id, int count) {
        Cart cart = cartService.getCurrentCart();
        if (cart == null) {
            return false;
        }

        OrderLine orderLine = new OrderLine();
        orderLine.setCount(count);
        orderLine.setProduct(productRepository.getOne(id));
        orderLine.setCart(cart);

        orderLineRepository.save(orderLine);
        return true;
    }

    public boolean addComment(Long id, Comment comment) {
        Consumer consumer = consumerService.getCurrentConsumer();

        if (consumer == null) {
            return false;
        }

        comment.setAuthor(consumer);
        comment.setProduct(productRepository.getOne(id));
        commentRepository.save(comment);

        return true;
    }

    public Page<Product> getProductPage(int currentPage, int pageSize, int sortType) {
        return productRepository.findAll(this.getPageRequest(currentPage, pageSize, sortType));
    }

    public Page<Product> getProductPage(int currentPage, int pageSize, int sortType, String tag) {
        return productRepository.findByTag(tag, this.getPageRequest(currentPage, pageSize, sortType));
    }

    private PageRequest getPageRequest(int currentPage, int pageSize, int sortType) {
        switch (sortType) {
            case 1:
                return PageRequest.of(currentPage - 1, pageSize, Sort.Direction.ASC, "name");
            case 2:
                return PageRequest.of(currentPage - 1, pageSize, Sort.Direction.DESC, "name");
            case 3:
                return PageRequest.of(currentPage - 1, pageSize, Sort.Direction.ASC, "price");
            case 4:
                return PageRequest.of(currentPage - 1, pageSize, Sort.Direction.DESC, "price");
            default:
                return PageRequest.of(currentPage - 1, pageSize, Sort.Direction.DESC, "id");

        }
    }
}
