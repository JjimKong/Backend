package com.jjimkong_backend.api.service.review;

import com.jjimkong_backend.api.service.review.dto.request.ReviewSaveRequest;
import com.jjimkong_backend.api.service.review.dto.request.ReviewUpdateRequest;
import com.jjimkong_backend.api.service.review.dto.response.ReviewResponse;
import com.jjimkong_backend.domain.users.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {

    Long createReview(Long postId, ReviewSaveRequest request, List<MultipartFile> photos, User user);

    List<ReviewResponse> getReviewsByPost(Long postId);

    ReviewResponse getReview(Long reviewId);

    Long updateReview(Long reviewId, ReviewUpdateRequest request, List<MultipartFile> photos, Long userId);

    void deleteReview(Long reviewId, Long userId);
}
