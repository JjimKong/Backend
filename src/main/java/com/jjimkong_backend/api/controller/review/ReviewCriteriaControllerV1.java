package com.jjimkong_backend.api.controller.review;

import com.jjimkong_backend.api.service.review.ReviewCriteriaService;
import com.jjimkong_backend.api.service.review.dto.request.ReviewCriteriaSaveRequest;
import com.jjimkong_backend.api.service.review.dto.request.ReviewCriteriaUpdateRequest;
import com.jjimkong_backend.api.service.review.dto.response.ReviewCriteriaResponse;
import com.jjimkong_backend.global.response.api.ApiResponse;
import com.jjimkong_backend.global.response.exception.ExceptionCode;
import com.jjimkong_backend.global.response.exception.annotation.SwaggerExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ReviewCriteria", description = "평가 기준 API (조회는 인증 사용자, 추가/수정/삭제는 ADMIN 전용)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review-criteria")
@Slf4j
public class ReviewCriteriaControllerV1 {

    private final ReviewCriteriaService reviewCriteriaService;

    /**
     * ✅ 평가 기준 목록 조회
     * GET /api/v1/review-criteria
     */
    @Operation(
            summary = "평가 기준 목록 조회",
            description = """
                    리뷰 작성 폼에서 사용할 평가 기준 목록을 조회한다.

                    **인증 필요**
                    """
    )
    @GetMapping
    public ApiResponse<List<ReviewCriteriaResponse>> getCriteriaList() {
        log.info("ReviewCriteriaControllerV1.getCriteriaList");
        return ApiResponse.ok(reviewCriteriaService.getCriteriaList());
    }

    /**
     * ✅ 평가 기준 추가 (ADMIN)
     * POST /api/v1/review-criteria
     */
    @Operation(
            summary = "[ADMIN] 평가 기준 추가",
            description = "새 평가 기준을 추가한다. **ADMIN 권한 필요.**"
    )
    @PostMapping
    public ApiResponse<Long> saveCriteria(@Valid @RequestBody ReviewCriteriaSaveRequest request) {
        log.info("ReviewCriteriaControllerV1.saveCriteria - name: {}", request.name());
        return ApiResponse.created(reviewCriteriaService.saveCriteria(request));
    }

    /**
     * ✅ 평가 기준 수정 (ADMIN)
     * PUT /api/v1/review-criteria/{criteriaId}
     */
    @Operation(
            summary = "[ADMIN] 평가 기준 수정",
            description = "평가 기준 이름을 수정한다. **ADMIN 권한 필요.**"
    )
    @SwaggerExceptionResponse({ExceptionCode.NOT_FOUND_REVIEW_CRITERIA})
    @PutMapping("/{criteriaId}")
    public ApiResponse<Long> updateCriteria(@PathVariable Long criteriaId,
                                            @Valid @RequestBody ReviewCriteriaUpdateRequest request) {
        log.info("ReviewCriteriaControllerV1.updateCriteria - criteriaId: {}", criteriaId);
        return ApiResponse.ok(reviewCriteriaService.updateCriteria(criteriaId, request));
    }

    /**
     * ✅ 평가 기준 삭제 (ADMIN)
     * DELETE /api/v1/review-criteria/{criteriaId}
     */
    @Operation(
            summary = "[ADMIN] 평가 기준 삭제",
            description = "평가 기준을 삭제(소프트)한다. **ADMIN 권한 필요.**"
    )
    @SwaggerExceptionResponse({ExceptionCode.NOT_FOUND_REVIEW_CRITERIA})
    @DeleteMapping("/{criteriaId}")
    public ApiResponse<Void> deleteCriteria(@PathVariable Long criteriaId) {
        log.info("ReviewCriteriaControllerV1.deleteCriteria - criteriaId: {}", criteriaId);
        reviewCriteriaService.deleteCriteria(criteriaId);
        return ApiResponse.noContent();
    }
}
