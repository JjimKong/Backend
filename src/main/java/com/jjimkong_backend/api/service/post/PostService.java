package com.jjimkong_backend.api.service.post;

import com.jjimkong_backend.api.service.post.dto.request.PostSaveRequest;
import com.jjimkong_backend.api.service.post.dto.request.PostUpdateRequest;
import com.jjimkong_backend.api.service.post.dto.response.PostResponse;
import com.jjimkong_backend.domain.users.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<PostResponse> getPostList(User user);

    PostResponse getPost(Long postId);

    Long savePost(PostSaveRequest request, List<MultipartFile> images, User user);

    Long updatePost(Long postId, PostUpdateRequest request, List<MultipartFile> images, Long userId);

    void deletePost(Long postId, Long userId);
}
