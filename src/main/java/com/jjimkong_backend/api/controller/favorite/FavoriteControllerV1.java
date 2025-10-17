package com.jjimkong_backend.api.controller.favorite;

import com.jjimkong_backend.api.service.favorite.FavoriteService;
import com.jjimkong_backend.api.service.favorite.dto.response.FavoriteListResponse;
import com.jjimkong_backend.global.config.oauth2.CustomOAuth2User;
import com.jjimkong_backend.global.response.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@Slf4j
public class FavoriteControllerV1 {

    private final FavoriteService favoriteService;

    @GetMapping
    public ApiResponse<FavoriteListResponse> getFavorites(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Long userId = customOAuth2User.getUser().getId();
        log.info("FavoriteControllerV1.getFavorites - userId: {}", userId);

        FavoriteListResponse response = favoriteService.getFavoriteList(customOAuth2User.getUser());
        return ApiResponse.ok(response);
    }

    @PostMapping("/{postId}")
    public ApiResponse<Void> addFavorite(
        @PathVariable Long postId,
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Long userId = customOAuth2User.getUser().getId();
        log.info("FavoriteControllerV1.addFavorite - userId: {}, postId: {}", userId, postId);

        favoriteService.addFavorite(postId, customOAuth2User.getUser());
        return ApiResponse.created(null);
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Void> removeFavorite(
        @PathVariable Long postId,
        @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Long userId = customOAuth2User.getUser().getId();
        log.info("FavoriteControllerV1.removeFavorite - userId: {}, postId: {}", userId, postId);

        favoriteService.removeFavorite(postId, customOAuth2User.getUser());
        return ApiResponse.noContent();
    }
}
