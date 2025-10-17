package com.jjimkong_backend.api.service.favorite.dto.response;

import java.util.List;

public record FavoriteListResponse(List<FavoriteResponse> favorites) {

    public static FavoriteListResponse of(List<FavoriteResponse> favorites) {
        return new FavoriteListResponse(favorites);
    }
}
