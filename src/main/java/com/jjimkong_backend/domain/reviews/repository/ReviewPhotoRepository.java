package com.jjimkong_backend.domain.reviews.repository;

import com.jjimkong_backend.domain.reviews.entity.ReviewPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, Long> {

    List<ReviewPhoto> findByReviewId(Long reviewId);
}
