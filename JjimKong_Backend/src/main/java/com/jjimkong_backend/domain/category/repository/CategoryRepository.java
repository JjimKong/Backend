package com.jjimkong_backend.domain.category.repository;

import com.jjimkong_backend.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
