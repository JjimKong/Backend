package com.jjimkong_backend.api.service.post.dto.request;

import com.jjimkong_backend.domain.posts.entity.Category;

public record PostUpdateRequest(
        String region,
        String detailAddress,
        String restaurantName,
        Category category
) {
}
