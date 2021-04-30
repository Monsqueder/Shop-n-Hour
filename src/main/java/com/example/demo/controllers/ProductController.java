package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.repos.*;
import com.example.demo.services.CartService;
import com.example.demo.services.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Controller
public class ProductController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ConsumerService userService;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @GetMapping("/addProduct")
    public String getProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "add_product";
    }

    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null) {
            File directory = new File(uploadPath);
            if(!directory.exists()) {
                directory.mkdir();
            }
            String uniqueName = UUID.randomUUID().toString();
            String fileName = uniqueName + "." + file.getOriginalFilename();
            product.setImg_name(fileName);
            file.transferTo(new File(uploadPath + "/" + fileName));
        }
        productRepository.save(product);
        return "redirect:/";
    }

    @GetMapping("/product/{id}")
    public String getProductPage(Model model, @PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return "redirect:/";
        }
        Product product = productRepository.getOne(id);
        model.addAttribute("product", product);
        model.addAttribute("comments", product.getComments());
        return "product";
    }

    @PostMapping("/addToCart/{id}")
    public String addToCart(@PathVariable Long id, @RequestParam int count) {
        if (cartService.addToCart(id, count)) {
            return "redirect:/";
        } else {
            return "redirect:/product/{id}";
        }
    }

    @PostMapping("/addComment/{id}")
    public String addComment(@PathVariable Long id, @ModelAttribute Comment comment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        comment.setAuthor(consumerRepository.findByEmail(authentication.getName()));
        comment.setProduct(productRepository.getOne(id));
        commentRepository.save(comment);
        return "redirect:/product/{id}";
    }

    @GetMapping("/cart")
    public String getCart(Model model) {
        Cart cart = cartService.getCurrentCart(userService.getCurrentConsumer());
        model.addAttribute("cart", cart);
        if (cart.getOrderLines() != null) {
            model.addAttribute("isEmpty", cart.getOrderLines().isEmpty());
        } else {
            model.addAttribute("isEmpty", true);
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
