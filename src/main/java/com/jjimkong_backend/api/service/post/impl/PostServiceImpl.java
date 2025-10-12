package com.jjimkong_backend.api.service.post.impl;

import com.jjimkong_backend.api.service.post.PostService;
import com.jjimkong_backend.api.service.post.dto.request.PostSaveRequest;
import com.jjimkong_backend.api.service.post.dto.request.PostUpdateRequest;
import com.jjimkong_backend.api.service.post.dto.response.PostResponse;
import com.jjimkong_backend.domain.posts.entity.Post;
import com.jjimkong_backend.domain.posts.repository.PostRepository;
import com.jjimkong_backend.domain.users.entity.User;
import com.jjimkong_backend.global.response.exception.BadRequestException;
import com.jjimkong_backend.global.response.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<PostResponse> getPostList(User user) {
        return postRepository.findByUser(user).stream()
                .map(PostResponse::from)
                .toList();
    }

    @Override
    public PostResponse getPost(Long postId) {
        Post post = findPostById(postId);

        return PostResponse.from(post);
    }

    @Override
    @Transactional
    public Long savePost(PostSaveRequest request, User user) {

        Post post = request.toEntity(user);
        return postRepository.save(post).getId();
    }

    @Override
    @Transactional
    public Long updatePost(Long postId, PostUpdateRequest request, Long userId) {
        Post post = findPostById(postId);
        if (!post.getUser().getId().equals(userId)) {
            throw new BadRequestException(ExceptionCode.NO_PERMISSION_TO_UPDATE_POST);
        }

        post.update(request.region(), request.detailAddress(), request.restaurantName(), request.category());
        return postId;
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = findPostById(postId);
        if (!post.getUser().getId().equals(userId)) {
            throw new BadRequestException(ExceptionCode.NO_PERMISSION_TO_DELETE_POST);
        }
        postRepository.delete(post);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_POST));
    }

}