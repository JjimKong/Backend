package com.jjimkong_backend.domain.reviews.repository;

import com.jjimkong_backend.domain.reviews.entity.ReviewScore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewScoreRepository extends JpaRepository<ReviewScore, Long> {
}
