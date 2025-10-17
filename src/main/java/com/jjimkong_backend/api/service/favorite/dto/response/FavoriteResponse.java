package com.jjimkong_backend.api.service.favorite.dto.response;

import com.jjimkong_backend.domain.favorites.entity.Favorite;

public record FavoriteResponse(Long favoriteId, Long postId) {

    public static FavoriteResponse from(Favorite favorite) {
        Long resolvedPostId = favorite.getPost() != null ? favorite.getPost().getId() : null;
        return new FavoriteResponse(favorite.getId(), resolvedPostId);
    }
}
