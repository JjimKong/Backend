package com.jjimkong_backend.api.service.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "리뷰 생성 요청 (multipart 의 request 파트, application/json)")
public record ReviewSaveRequest(

        @Schema(description = "리뷰 본문", example = "분위기도 좋고 맛있어요")
        String content,

        @Schema(description = "평가 기준별 점수 목록")
        List<ScoreRequest> scores
) {
    @Schema(description = "기준별 점수")
    public record ScoreRequest(

            @Schema(description = "평가 기준 ID", example = "1")
            Long criteriaId,

            @Schema(description = "점수", example = "4.5")
            Double score
    ) {
    }
}
