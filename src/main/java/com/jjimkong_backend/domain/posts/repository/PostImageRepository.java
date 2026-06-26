package com.jjimkong_backend.domain.posts.repository;

import com.jjimkong_backend.domain.posts.entity.Post;
import com.jjimkong_backend.domain.posts.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByPost(Post post);

    List<PostImage> findByPostId(Long postId);
}
