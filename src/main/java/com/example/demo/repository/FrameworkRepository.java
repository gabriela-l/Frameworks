package com.example.demo.repository;

import com.example.demo.model.Framework;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrameworkRepository extends JpaRepository<Framework, Integer> {
    Long countById(Integer id);
}
