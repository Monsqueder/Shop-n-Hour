package com.example.demo.repos;

import com.example.demo.models.Category;
import com.example.demo.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);

    Page<Product> findAllByCategory(Category category, Pageable pageable);

    @Query(value = "SELECT p FROM Product p where LOWER(p.name) like :tag%")
    Page<Product> findByTag(@Param("tag") String tag, Pageable pageable);

    @Modifying
    @Query("UPDATE Product p SET p.name = :name, p.company = :company, p.price = :price, p.description = :description, p.img_name = :img_name where p.id = :id")
    void update(@Param("id") Long id,
                @Param("name") String name,
                @Param("company") String company,
                @Param("price") Double price,
                @Param("description") String description,
                @Param("img_name") String img_name);
}
