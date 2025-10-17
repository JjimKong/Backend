package com.jjimkong_backend.domain.favorites.repository;

import com.jjimkong_backend.domain.favorites.entity.Favorite;
import com.jjimkong_backend.domain.users.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByUser(User user);

    Optional<Favorite> findByPost_IdAndUser(Long postId, User user);

    boolean existsByPost_IdAndUser(Long postId, User user);
}
