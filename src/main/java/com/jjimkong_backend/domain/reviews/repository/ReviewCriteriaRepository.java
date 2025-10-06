package com.jjimkong_backend.domain.reviews.repository;

import com.jjimkong_backend.domain.reviews.entity.ReviewCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCriteriaRepository extends JpaRepository<ReviewCriteria, Long> {
}
