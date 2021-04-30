package com.example.demo.repos;

import com.example.demo.models.Cart;
import com.example.demo.models.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByConsumer(Consumer consumer);

    Cart findAllByConsumer(Consumer consumer);
}
