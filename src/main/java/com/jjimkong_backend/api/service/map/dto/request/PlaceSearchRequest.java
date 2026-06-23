package com.jjimkong_backend.api.service.map.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * 장소(맛집) 키워드 검색 요청.
 * <p>
 * GET 쿼리 파라미터로 바인딩된다. (예: {@code ?query=강남 맛집&display=5&sort=random})
 *
 * @param query   검색어 (필수)
 * @param display 검색 결과 개수 (1~5, 기본 5)
 * @param sort    정렬 방식 (random: 정확도순, comment: 리뷰 많은 순. 기본 random)
 */
public record PlaceSearchRequest(

        @Schema(description = "검색어", example = "강남 초밥", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "검색어를 입력해 주세요.")
        String query,

        @Schema(description = "검색 결과 개수 (1~5)", example = "5", defaultValue = "5",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED, minimum = "1", maximum = "5")
        @Min(value = 1, message = "검색 결과 개수는 1 이상이어야 합니다.")
        @Max(value = 5, message = "검색 결과 개수는 최대 5개입니다.")
        Integer display,

        @Schema(description = "정렬 방식 (random: 정확도순, comment: 리뷰 많은 순)",
                example = "random", defaultValue = "random",
                allowableValues = {"random", "comment"},
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        String sort
) {

    private static final int DEFAULT_DISPLAY = 5;
    private static final String DEFAULT_SORT = "random";

    public int displayOrDefault() {
        return display != null ? display : DEFAULT_DISPLAY;
    }

    public String sortOrDefault() {
        return (sort != null && !sort.isBlank()) ? sort : DEFAULT_SORT;
    }
}
