package com.example.demo.services;

import com.example.demo.models.*;
import com.example.demo.repos.CategoryRepository;
import com.example.demo.repos.CommentRepository;
import com.example.demo.repos.OrderLineRepository;
import com.example.demo.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private CategoryRepository categoryRepository;

    public boolean addToCart(long id, int count, String color, String size) {
        Cart cart = cartService.getCurrentCart();
        if (cart == null) {
            return false;
        }

        OrderLine orderLine = new OrderLine();
        Product product = productRepository.getOne(id);
        orderLine.setProduct(product);
        orderLine.setPrice(product.getPrice());
        orderLine.setColor(color);
        orderLine.setSize(size);
        orderLine.setCount(count);
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
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            if (tag.toLowerCase().equals(category.getName().toLowerCase())) {
                return productRepository.findAllByCategory(category, this.getPageRequest(currentPage, pageSize, sortType));
            }
        }
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

    public List<Product> getProductLine(int type, int count) {
        List<Product> fullList = productRepository.findAll();
        if (fullList.size() == 0) {
            return null;
        }
        if (fullList.size() < count) {
            count = fullList.size();
        }
        switch (type) {
            case 1:
                ArrayList<Product> list1 = new ArrayList<>();
                boolean justice = true;
                while (justice) {
                    int random = (int) (Math.random()*fullList.size());
                    if (!list1.contains(fullList.get(random))) {
                        list1.add(fullList.get(random));
                    }
                    if (list1.size() == count) {
                        justice = false;
                    }
                }
                return list1;
            case 2:
                ArrayList<Product> list2 = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    list2.add(fullList.get(i));
                }
                return list2;
            default:
                return null;
        }
    }
}
