package com.example.demo.controllers;

import com.example.demo.models.Product;
import com.example.demo.repos.ConsumerRepository;
import com.example.demo.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private ProductRepository productRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @GetMapping("/")
    public String getMainPage(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("sort") Optional<Integer> sort) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
             model.addAttribute("consumer", consumerRepository.findByEmail(authentication.getName()));
        }

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        int sortType = sort.orElse(0);

        Page<Product> productsPage = productRepository.findAll(getPageRequest(currentPage, pageSize, sortType));

        int totalCount = productsPage.getTotalPages();

        model.addAttribute("productsPage", productsPage);

        if (totalCount > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalCount)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "main";
    }

    public PageRequest getPageRequest(int currentPage, int pageSize, int sortType) {
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
