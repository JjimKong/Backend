package com.jjimkong_backend.domain.favorites.repository;

import com.jjimkong_backend.domain.favorites.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}
