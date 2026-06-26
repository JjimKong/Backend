package com.jjimkong_backend.api.service.review.dto.request;

import com.jjimkong_backend.api.service.review.dto.request.ReviewSaveRequest.ScoreRequest;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "리뷰 수정 요청 (multipart 의 request 파트, application/json)")
public record ReviewUpdateRequest(

        @Schema(description = "리뷰 본문", example = "재방문 의사 있어요")
        String content,

        @Schema(description = "평가 기준별 점수 목록 (전달 시 기존 점수 교체)")
        List<ScoreRequest> scores
) {
}
