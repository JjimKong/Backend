package com.jjimkong_backend.api.service.post.dto.response;

import com.jjimkong_backend.domain.posts.entity.Category;
import com.jjimkong_backend.domain.posts.entity.Post;

public record PostResponse(
        Long id,
        String region,
        String detailAddress,
        String restaurantName,
        String restaurantUid,
        Long userId,
        Category category
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getRegion(),
                post.getDetailAddress(),
                post.getRestaurantName(),
                post.getRestaurantUid(),
                post.getUser().getId(),
                post.getCategory()
        );
    }
}
