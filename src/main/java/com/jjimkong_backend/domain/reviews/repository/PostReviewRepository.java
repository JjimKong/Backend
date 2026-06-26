package com.jjimkong_backend.domain.reviews.repository;

import com.jjimkong_backend.domain.reviews.entity.PostReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostReviewRepository extends JpaRepository<PostReview, Long> {

    List<PostReview> findByPostId(Long postId);

    Optional<PostReview> findByReviewId(Long reviewId);
}
