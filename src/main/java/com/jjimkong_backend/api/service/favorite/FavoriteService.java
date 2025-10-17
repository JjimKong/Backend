package com.jjimkong_backend.api.service.favorite;

import com.jjimkong_backend.api.service.favorite.dto.response.FavoriteListResponse;
import com.jjimkong_backend.domain.users.entity.User;

public interface FavoriteService {

    FavoriteListResponse getFavoriteList(User user);

    void addFavorite(Long postId, User user);

    void removeFavorite(Long postId, User user);
}
