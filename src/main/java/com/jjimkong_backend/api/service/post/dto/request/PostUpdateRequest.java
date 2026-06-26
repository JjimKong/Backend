package com.jjimkong_backend.api.service.post.dto.request;

import com.jjimkong_backend.domain.posts.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 게시물 수정 요청. (텍스트 정보만, 이미지 제외)
 *
 * @param region         지역
 * @param detailAddress  상세 주소
 * @param restaurantName 가게(맛집) 이름
 * @param category       카테고리
 */
@Schema(description = "게시물 수정 요청")
public record PostUpdateRequest(

        @Schema(description = "지역", example = "서울 강남구")
        String region,

        @Schema(description = "상세 주소", example = "테헤란로 123")
        String detailAddress,

        @Schema(description = "가게(맛집) 이름", example = "스시 오마카세 강남점")
        String restaurantName,

        @Schema(description = "카테고리", example = "CAFE",
                allowableValues = {"RESTAURANT", "CAFE", "ACCOMMODATION", "ACTIVITY", "ETC"})
        Category category
) {
}
