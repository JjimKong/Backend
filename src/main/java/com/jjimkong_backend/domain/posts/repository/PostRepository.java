package com.jjimkong_backend.domain.posts.repository;

import com.jjimkong_backend.domain.posts.entity.Post;
import com.jjimkong_backend.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
}
