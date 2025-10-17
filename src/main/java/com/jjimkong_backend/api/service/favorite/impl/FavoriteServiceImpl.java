package com.jjimkong_backend.api.service.favorite.impl;

import com.jjimkong_backend.api.service.favorite.FavoriteService;
import com.jjimkong_backend.api.service.favorite.dto.response.FavoriteListResponse;
import com.jjimkong_backend.api.service.favorite.dto.response.FavoriteResponse;
import com.jjimkong_backend.domain.favorites.entity.Favorite;
import com.jjimkong_backend.domain.favorites.repository.FavoriteRepository;
import com.jjimkong_backend.domain.posts.entity.Post;
import com.jjimkong_backend.domain.posts.repository.PostRepository;
import com.jjimkong_backend.domain.users.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PostRepository postRepository;

    @Override
    public FavoriteListResponse getFavoriteList(User user) {
        List<FavoriteResponse> favorites = favoriteRepository.findAllByUser(user)
            .stream()
            .map(FavoriteResponse::from)
            .toList();

        return FavoriteListResponse.of(favorites);
    }

    @Override
    @Transactional
    public void addFavorite(Long postId, User user) {
        if (favoriteRepository.existsByPost_IdAndUser(postId, user)) {
            return;
        }

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));

        favoriteRepository.save(Favorite.builder()
            .post(post)
            .user(user)
            .build());
    }

    @Override
    @Transactional
    public void removeFavorite(Long postId, User user) {
        favoriteRepository.findByPost_IdAndUser(postId, user)
            .ifPresent(favoriteRepository::delete);
    }
}
