package com.jjimkong_backend.domain.posts.repository;

import com.jjimkong_backend.domain.posts.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
