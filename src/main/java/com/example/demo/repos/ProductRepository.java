package com.example.demo.repos;

import com.example.demo.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);

    @Query(value = "SELECT p FROM Product p where LOWER(p.name) like :tag%")
    Page<Product> findByTag(@Param("tag") String tag, Pageable pageable);
}
