package com.jjimkong_backend.api.service.post.dto.request;

import com.jjimkong_backend.domain.posts.entity.Category;
import com.jjimkong_backend.domain.posts.entity.Post;
import com.jjimkong_backend.domain.users.entity.User;

public record PostSaveRequest(
        String region,
        String detailAddress,
        String restaurantName,
        String restaurantUid,
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
