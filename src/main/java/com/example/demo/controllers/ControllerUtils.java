package com.example.demo.controllers;

import com.example.demo.models.Product;
import com.example.demo.repos.CategoryRepository;
import com.example.demo.services.ConsumerService;
import com.example.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ControllerUtils {

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    public void showErrors(Model model, BindingResult bindingResult) {
        Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage));
        model.mergeAttributes(errors);
    }

    public void showHeader(Model model, String tag) {
        List<String> ideas = Arrays.asList("idea1", "idea2", "idea3");

        model.addAttribute("consumer", consumerService.getCurrentConsumer());
        if (consumerService.isAdmin()) {
            model.addAttribute("isAdmin", true);
        } else {
            model.addAttribute("isAdmin", false);
        }
        if (consumerService.isAdmin()) {
            model.addAttribute("isModerator", true);
        } else {
            model.addAttribute("isModerator", false);
        }
        model.addAttribute("ideas", ideas);

        if (tag == null) {
            model.addAttribute("tag", "");
        } else {
            model.addAttribute("tag", tag);
        }

        model.addAttribute("maleCategories", categoryRepository.findAllByType("male"));
        model.addAttribute("femaleCategories", categoryRepository.findAllByType("female"));
    }

    public void showProductPage(Model model, Optional<Integer> page, Optional<Integer> size, Optional<Integer> sort, String tag) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        int sortType = sort.orElse(0);

        Page<Product> productPage;

        if (tag == null) {
            productPage = productService.getProductPage(currentPage, pageSize, sortType);
        } else {
            productPage = productService.getProductPage(currentPage, pageSize, sortType, tag.toLowerCase());
        }

        int totalCount = productPage.getTotalPages();
        if (totalCount > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalCount)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("productsPage", productPage);
    }
}
