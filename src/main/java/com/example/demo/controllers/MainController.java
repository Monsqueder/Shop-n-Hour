package com.example.demo.controllers;

import com.example.demo.models.Product;
import com.example.demo.services.ConsumerService;
import com.example.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MainController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ConsumerService consumerService;

    @GetMapping("/")
    public String getMainPage(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("sort") Optional<Integer> sort) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        int sortType = sort.orElse(0);

        Page<Product> productPage = productService.getProductPage(currentPage, pageSize, sortType);
        int totalCount = productPage.getTotalPages();
        if (totalCount > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalCount)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("consumer", consumerService.getCurrentConsumer());
        model.addAttribute("productsPage", productPage);

        return "main";
    }

}
