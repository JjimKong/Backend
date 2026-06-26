package com.jjimkong_backend.api.service.review.dto.response;

import com.jjimkong_backend.domain.reviews.entity.ReviewCriteria;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "평가 기준 응답")
public record ReviewCriteriaResponse(

        @Schema(description = "기준 ID", example = "1")
        Long id,

        @Schema(description = "기준 이름", example = "맛")
        String name
) {
    public static ReviewCriteriaResponse from(ReviewCriteria criteria) {
        return new ReviewCriteriaResponse(criteria.getId(), criteria.getName());
    }
}
