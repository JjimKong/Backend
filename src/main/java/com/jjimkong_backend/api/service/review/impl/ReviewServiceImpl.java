package com.jjimkong_backend.api.service.review.impl;

import com.jjimkong_backend.api.service.review.ReviewService;
import com.jjimkong_backend.api.service.review.dto.request.ReviewSaveRequest;
import com.jjimkong_backend.api.service.review.dto.request.ReviewSaveRequest.ScoreRequest;
import com.jjimkong_backend.api.service.review.dto.request.ReviewUpdateRequest;
import com.jjimkong_backend.api.service.review.dto.response.ReviewResponse;
import com.jjimkong_backend.api.service.review.dto.response.ReviewResponse.ScoreResponse;
import com.jjimkong_backend.domain.posts.entity.Post;
import com.jjimkong_backend.domain.posts.repository.PostRepository;
import com.jjimkong_backend.domain.reviews.entity.PostReview;
import com.jjimkong_backend.domain.reviews.entity.Review;
import com.jjimkong_backend.domain.reviews.entity.ReviewCriteria;
import com.jjimkong_backend.domain.reviews.entity.ReviewPhoto;
import com.jjimkong_backend.domain.reviews.entity.ReviewScore;
import com.jjimkong_backend.domain.reviews.repository.PostReviewRepository;
import com.jjimkong_backend.domain.reviews.repository.ReviewCriteriaRepository;
import com.jjimkong_backend.domain.reviews.repository.ReviewPhotoRepository;
import com.jjimkong_backend.domain.reviews.repository.ReviewRepository;
import com.jjimkong_backend.domain.reviews.repository.ReviewScoreRepository;
import com.jjimkong_backend.domain.users.entity.User;
import com.jjimkong_backend.global.config.s3.S3Service;
import com.jjimkong_backend.global.response.exception.BadRequestException;
import com.jjimkong_backend.global.response.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private static final String REVIEW_PHOTO_DIR = "reviews";

    private final ReviewRepository reviewRepository;
    private final PostReviewRepository postReviewRepository;
    private final ReviewScoreRepository reviewScoreRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;
    private final ReviewCriteriaRepository reviewCriteriaRepository;
    private final PostRepository postRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public Long createReview(Long postId, ReviewSaveRequest request, List<MultipartFile> photos, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_POST));

        Review review = reviewRepository.save(new Review(request.content(), user));
        postReviewRepository.save(new PostReview(post, review));
        saveScores(review, request.scores());
        uploadPhotos(review, photos);
        return review.getId();
    }

    @Override
    public List<ReviewResponse> getReviewsByPost(Long postId) {
        return postReviewRepository.findByPostId(postId).stream()
                .map(postReview -> toResponse(postReview.getReview(), postId))
                .toList();
    }

    @Override
    public ReviewResponse getReview(Long reviewId) {
        Review review = findReviewById(reviewId);
        Long postId = postReviewRepository.findByReviewId(reviewId)
                .map(postReview -> postReview.getPost().getId())
                .orElse(null);
        return toResponse(review, postId);
    }

    @Override
    @Transactional
    public Long updateReview(Long reviewId, ReviewUpdateRequest request, List<MultipartFile> photos, Long userId) {
        Review review = findReviewById(reviewId);
        if (!review.getUser().getId().equals(userId)) {
            throw new BadRequestException(ExceptionCode.NO_PERMISSION_TO_UPDATE_REVIEW);
        }

        review.update(request.content());

        // 점수가 전달된 경우에만 기존 점수 교체
        if (request.scores() != null) {
            reviewScoreRepository.findByReviewId(reviewId).forEach(ReviewScore::delete);
            saveScores(review, request.scores());
        }
        // 사진이 전달된 경우에만 기존 사진 교체(소프트 삭제, S3 객체는 보존)
        if (photos != null) {
            reviewPhotoRepository.findByReviewId(reviewId).forEach(ReviewPhoto::delete);
            uploadPhotos(review, photos);
        }
        return reviewId;
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = findReviewById(reviewId);
        if (!review.getUser().getId().equals(userId)) {
            throw new BadRequestException(ExceptionCode.NO_PERMISSION_TO_DELETE_REVIEW);
        }
        reviewScoreRepository.findByReviewId(reviewId).forEach(ReviewScore::delete);
        reviewPhotoRepository.findByReviewId(reviewId).forEach(ReviewPhoto::delete);
        postReviewRepository.findByReviewId(reviewId).ifPresent(PostReview::delete);
        review.delete();
    }

    private void saveScores(Review review, List<ScoreRequest> scores) {
        if (scores == null) {
            return;
        }
        for (ScoreRequest scoreRequest : scores) {
            ReviewCriteria criteria = reviewCriteriaRepository.findById(scoreRequest.criteriaId())
                    .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_REVIEW_CRITERIA));
            reviewScoreRepository.save(new ReviewScore(scoreRequest.score(), criteria, review));
        }
    }

    private void uploadPhotos(Review review, List<MultipartFile> photos) {
        if (photos == null) {
            return;
        }
        for (MultipartFile photo : photos) {
            if (photo == null || photo.isEmpty()) {
                continue;
            }
            S3Service.UploadResult result = s3Service.upload(photo, REVIEW_PHOTO_DIR);
            reviewPhotoRepository.save(new ReviewPhoto(result.url(), review));
        }
    }

    private ReviewResponse toResponse(Review review, Long postId) {
        List<ScoreResponse> scores = reviewScoreRepository.findByReviewId(review.getId()).stream()
                .map(score -> new ScoreResponse(
                        score.getReviewCriteria().getId(),
                        score.getReviewCriteria().getName(),
                        score.getScore()))
                .toList();
        List<String> photoUrls = reviewPhotoRepository.findByReviewId(review.getId()).stream()
                .map(ReviewPhoto::getPhotoUrl)
                .toList();
        return new ReviewResponse(review.getId(), postId, review.getUser().getId(), review.getContent(), scores, photoUrls);
    }

    private Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_REVIEW));
    }
}
