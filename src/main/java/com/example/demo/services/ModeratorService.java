package com.example.demo.services;

import com.example.demo.models.Product;
import com.example.demo.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ModeratorService {

    @Autowired
    private ProductRepository productRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public boolean addProduct(Product product, MultipartFile file) {

        if (product == null || file == null) {
            return false;
        }

        File directory = new File(uploadPath);
        if(!directory.exists()) {
            directory.mkdir();
        }
        String uniqueName = UUID.randomUUID().toString();
        String fileName = uniqueName + "." + file.getOriginalFilename();
        product.setImg_name(fileName);
        try {
            file.transferTo(new File(uploadPath + "/" + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        productRepository.save(product);

        return true;
    }

}
