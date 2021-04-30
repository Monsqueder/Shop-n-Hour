package com.example.demo.repos;

import com.example.demo.models.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
    Consumer findByEmail(String email);

    Consumer findByActivationCode(String code);
}
