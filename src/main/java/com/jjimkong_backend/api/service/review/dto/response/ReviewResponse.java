package com.jjimkong_backend.api.service.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "리뷰 응답")
public record ReviewResponse(

        @Schema(description = "리뷰 ID", example = "1")
        Long id,

        @Schema(description = "대상 게시물(Post) ID", example = "10")
        Long postId,

        @Schema(description = "작성자 ID", example = "1")
        Long userId,

        @Schema(description = "리뷰 본문", example = "분위기도 좋고 맛있어요")
        String content,

        @Schema(description = "기준별 점수 목록")
        List<ScoreResponse> scores,

        @Schema(description = "리뷰 사진 URL 목록")
        List<String> photoUrls
) {
    @Schema(description = "기준별 점수")
    public record ScoreResponse(

            @Schema(description = "평가 기준 ID", example = "1")
            Long criteriaId,

            @Schema(description = "평가 기준 이름", example = "맛")
            String criteriaName,

            @Schema(description = "점수", example = "4.5")
            Double score
    ) {
    }
}
