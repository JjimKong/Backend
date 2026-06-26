package com.jjimkong_backend.api.service.post.dto.request;

import com.jjimkong_backend.domain.posts.entity.Category;
import com.jjimkong_backend.domain.posts.entity.Post;
import com.jjimkong_backend.domain.users.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 게시물 생성 요청. (multipart 의 {@code request} 파트, application/json)
 *
 * @param region         지역
 * @param detailAddress  상세 주소
 * @param restaurantName 가게(맛집) 이름
 * @param restaurantUid  장소 고유 ID (네이버 검색 결과 등 외부 식별자)
 * @param category       카테고리 (필수)
 */
@Schema(description = "게시물 생성 요청")
public record PostSaveRequest(

        @Schema(description = "지역", example = "서울 강남구")
        String region,

        @Schema(description = "상세 주소", example = "테헤란로 123")
        String detailAddress,

        @Schema(description = "가게(맛집) 이름", example = "스시 오마카세 강남점")
        String restaurantName,

        @Schema(description = "장소 고유 ID (네이버 검색 결과 등 외부 식별자)", example = "place_12345")
        String restaurantUid,

        @Schema(description = "카테고리", example = "RESTAURANT",
                allowableValues = {"RESTAURANT", "CAFE", "ACCOMMODATION", "ACTIVITY", "ETC"},
                requiredMode = Schema.RequiredMode.REQUIRED)
        Category category
) {
    public Post toEntity(User user) {
        return new Post(
                this.region,
                this.detailAddress,
                this.restaurantName,
                this.restaurantUid,
                user,
                this.category
        );
    }
}
