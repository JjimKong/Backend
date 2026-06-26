package com.jjimkong_backend.api.controller.review;

import com.jjimkong_backend.api.service.review.ReviewService;
import com.jjimkong_backend.api.service.review.dto.request.ReviewSaveRequest;
import com.jjimkong_backend.api.service.review.dto.request.ReviewUpdateRequest;
import com.jjimkong_backend.api.service.review.dto.response.ReviewResponse;
import com.jjimkong_backend.global.config.oauth2.CustomOAuth2User;
import com.jjimkong_backend.global.response.api.ApiResponse;
import com.jjimkong_backend.global.response.exception.ExceptionCode;
import com.jjimkong_backend.global.response.exception.annotation.SwaggerExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Review", description = "리뷰 API")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewControllerV1 {

    private final ReviewService reviewService;

    /**
     * ✅ 리뷰 작성
     * POST /api/v1/posts/{postId}/reviews  (multipart/form-data)
     */
    @Operation(
            summary = "리뷰 작성 (사진 다중 업로드)",
            description = """
                    특정 맛집(Post)에 리뷰를 작성한다. `multipart/form-data`.

                    - `request` 파트(application/json): 본문 + 기준별 점수 목록(`scores: [{criteriaId, score}]`)
                    - `photos` 파트(선택): 리뷰 사진들. S3에 업로드된다.

                    **인증 필요**
                    """
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    encoding = @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE)
            )
    )
    @SwaggerExceptionResponse({ExceptionCode.NOT_FOUND_POST, ExceptionCode.NOT_FOUND_REVIEW_CRITERIA,
            ExceptionCode.INVALID_IMAGE_FILE, ExceptionCode.IMAGE_UPLOAD_FAILED})
    @PostMapping(value = "/api/v1/posts/{postId}/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Long> createReview(
            @PathVariable Long postId,
            @RequestPart("request") ReviewSaveRequest request,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("ReviewControllerV1.createReview - userId: {}, postId: {}",
                customOAuth2User.getUser().getId(), postId);
        return ApiResponse.created(reviewService.createReview(postId, request, photos, customOAuth2User.getUser()));
    }

    /**
     * ✅ 특정 맛집의 리뷰 목록 조회
     * GET /api/v1/posts/{postId}/reviews
     */
    @Operation(
            summary = "맛집 리뷰 목록 조회",
            description = "특정 게시물(Post)에 달린 리뷰 목록을 조회한다. 각 리뷰에 기준별 점수·사진 URL 포함. **인증 필요**"
    )
    @GetMapping("/api/v1/posts/{postId}/reviews")
    public ApiResponse<List<ReviewResponse>> getReviewsByPost(@PathVariable Long postId) {
        log.info("ReviewControllerV1.getReviewsByPost - postId: {}", postId);
        return ApiResponse.ok(reviewService.getReviewsByPost(postId));
    }

    /**
     * ✅ 리뷰 상세 조회
     * GET /api/v1/reviews/{reviewId}
     */
    @Operation(
            summary = "리뷰 상세 조회",
            description = "리뷰 단건을 조회한다. **인증 필요**"
    )
    @SwaggerExceptionResponse({ExceptionCode.NOT_FOUND_REVIEW})
    @GetMapping("/api/v1/reviews/{reviewId}")
    public ApiResponse<ReviewResponse> getReview(@PathVariable Long reviewId) {
        log.info("ReviewControllerV1.getReview - reviewId: {}", reviewId);
        return ApiResponse.ok(reviewService.getReview(reviewId));
    }

    /**
     * ✅ 리뷰 수정
     * PUT /api/v1/reviews/{reviewId}  (multipart/form-data)
     */
    @Operation(
            summary = "리뷰 수정 (점수·사진 교체 가능)",
            description = """
                    리뷰를 수정한다. `multipart/form-data`. **본인 리뷰만 수정 가능.**

                    - `request` 파트(application/json): 본문 + 점수 목록(전달 시 기존 점수 교체)
                    - `photos` 파트(선택): 전달 시 기존 사진 전부 교체, 미전달 시 기존 유지

                    **인증 필요**
                    """
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    encoding = @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE)
            )
    )
    @SwaggerExceptionResponse({ExceptionCode.NOT_FOUND_REVIEW, ExceptionCode.NO_PERMISSION_TO_UPDATE_REVIEW,
            ExceptionCode.NOT_FOUND_REVIEW_CRITERIA, ExceptionCode.INVALID_IMAGE_FILE, ExceptionCode.IMAGE_UPLOAD_FAILED})
    @PutMapping(value = "/api/v1/reviews/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Long> updateReview(
            @PathVariable Long reviewId,
            @RequestPart("request") ReviewUpdateRequest request,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("ReviewControllerV1.updateReview - userId: {}, reviewId: {}",
                customOAuth2User.getUser().getId(), reviewId);
        return ApiResponse.ok(reviewService.updateReview(reviewId, request, photos, customOAuth2User.getUser().getId()));
    }

    /**
     * ✅ 리뷰 삭제
     * DELETE /api/v1/reviews/{reviewId}
     */
    @Operation(
            summary = "리뷰 삭제 (소프트 삭제)",
            description = "리뷰를 소프트 삭제한다(점수·사진·연결도 함께). **본인 리뷰만 삭제 가능 · 인증 필요**"
    )
    @SwaggerExceptionResponse({ExceptionCode.NOT_FOUND_REVIEW, ExceptionCode.NO_PERMISSION_TO_DELETE_REVIEW})
    @DeleteMapping("/api/v1/reviews/{reviewId}")
    public ApiResponse<Void> deleteReview(@PathVariable Long reviewId,
                                          @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("ReviewControllerV1.deleteReview - userId: {}, reviewId: {}",
                customOAuth2User.getUser().getId(), reviewId);
        reviewService.deleteReview(reviewId, customOAuth2User.getUser().getId());
        return ApiResponse.noContent();
    }
}
