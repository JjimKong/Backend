package com.jjimkong_backend.api.service.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "평가 기준 수정 요청")
public record ReviewCriteriaUpdateRequest(

        @Schema(description = "기준 이름", example = "친절도", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "기준 이름을 입력해 주세요.")
        String name
) {
}
