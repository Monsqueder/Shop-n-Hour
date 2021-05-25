package com.example.demo.controllers;

import com.example.demo.models.Consumer;
import com.example.demo.models.Product;
import com.example.demo.repos.ConsumerRepository;
import com.example.demo.services.AdminModeratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Controller
public class AdminModeratorController {

    @Autowired
    private AdminModeratorService adminModeratorService;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ControllerUtils controllerUtils;

    @GetMapping("/moderator")
    public String getModeratorPage(Model model) {

        //header
        controllerUtils.showHeader(model, null);

        model.addAttribute("sizes", new String[]{"xs", "s", "m", "l", "xl"});
        model.addAttribute("colors", new String[]{"black", "grey", "white", "red", "orange", "yellow", "blue"});
        model.addAttribute("product", new Product());
        return "moderator";
    }

    @PostMapping("/moderator/addProduct")
    public String addProduct(Model model,
                             @ModelAttribute Product product,
                             @RequestParam("files") MultipartFile[] files,
                             @RequestParam("selectedSizes") String[] sizes,
                             @RequestParam("selectedColors") String[] colors,
                             @Valid Long categoryId) {
        if (adminModeratorService.addProduct(product, categoryId, files, sizes, colors)) {
            model.addAttribute("message", "Added successfully!");
        } else {
            model.addAttribute("message", "Failed!");
        }
        getModeratorPage(model);
        return "moderator";
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model) {

        //header
        controllerUtils.showHeader(model, null);

        return "admin";
    }

    @PostMapping("/admin/findConsumer")
    public String findConsumer(Model model, @RequestParam String email) {
        Consumer consumer = consumerRepository.findByEmail(email);
        if (consumer != null) {
            model.addAttribute("foundConsumer", consumer);
        } else {
            model.addAttribute("message", "Consumer with email " + email + " not exists!");
        }
        return "admin";
    }

    @PostMapping("/admin/reset")
    public String resetPage() {
        return "redirect:/admin";
    }

    @PostMapping("/admin/makeModerator")
    public String makeConsumerModerator(Model model, @RequestParam boolean isModerator, @RequestParam Long id) {

        return "admin";
    }

    @PostMapping("/admin/block")
    public String blockConsumer(Model model, @RequestParam boolean isActive, @RequestParam Long id) {

        return "admin";
    }
}
