package com.jjimkong_backend.domain.reviews.repository;

import com.jjimkong_backend.domain.reviews.entity.PostReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReviewRepository extends JpaRepository<PostReview, Long> {
}
