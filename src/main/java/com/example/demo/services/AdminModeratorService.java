package com.example.demo.services;

import com.example.demo.models.Category;
import com.example.demo.models.Product;
import com.example.demo.repos.CategoryRepository;
import com.example.demo.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class AdminModeratorService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public boolean addProduct(Product product, Long categoryId, MultipartFile[] files, String[] sizes, String[] colors) {
        if (product == null || files == null) {
            return false;
        }

        List<String> fileNames = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file == null) {
                continue;
            }
            fileNames.add(this.addImage(file));
        }

        if (!categoryRepository.existsById(categoryId)) {
            return false;
        }
        Category category = categoryRepository.getOne(categoryId);

        product.setCategory(category);
        product.setImg_name(fileNames);
        product.setColors(Arrays.asList(colors));
        product.setSizes(Arrays.asList(sizes));

        productRepository.save(product);

        return true;
    }

    private String addImage(MultipartFile file) {
        File directory = new File(uploadPath);
        if(!directory.exists()) {
            directory.mkdir();
        }
        String uniqueName = UUID.randomUUID().toString();
        String fileName = uniqueName + "." + file.getOriginalFilename();
        try {
            file.transferTo(new File(uploadPath + "/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return fileName;
    }
}
