package com.jjimkong_backend.api.service.post.dto.response;

import com.jjimkong_backend.domain.posts.entity.Category;
import com.jjimkong_backend.domain.posts.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 게시물 응답.
 *
 * @param id             게시물 ID
 * @param region         지역
 * @param detailAddress  상세 주소
 * @param restaurantName 가게(맛집) 이름
 * @param restaurantUid  장소 고유 ID
 * @param userId         작성자 ID
 * @param category       카테고리
 * @param imageUrls      S3 이미지 URL 목록
 */
@Schema(description = "게시물 응답")
public record PostResponse(

        @Schema(description = "게시물 ID", example = "1")
        Long id,

        @Schema(description = "지역", example = "서울 강남구")
        String region,

        @Schema(description = "상세 주소", example = "테헤란로 123")
        String detailAddress,

        @Schema(description = "가게(맛집) 이름", example = "스시 오마카세 강남점")
        String restaurantName,

        @Schema(description = "장소 고유 ID", example = "place_12345")
        String restaurantUid,

        @Schema(description = "작성자 ID", example = "1")
        Long userId,

        @Schema(description = "카테고리", example = "RESTAURANT")
        Category category,

        @Schema(description = "S3 이미지 URL 목록",
                example = "[\"https://jjimkong-images-2026.s3.ap-northeast-2.amazonaws.com/posts/uuid.png\"]")
        List<String> imageUrls
) {
    public static PostResponse from(Post post, List<String> imageUrls) {
        return new PostResponse(
                post.getId(),
                post.getRegion(),
                post.getDetailAddress(),
                post.getRestaurantName(),
                post.getRestaurantUid(),
                post.getUser().getId(),
                post.getCategory(),
                imageUrls
        );
    }
}
