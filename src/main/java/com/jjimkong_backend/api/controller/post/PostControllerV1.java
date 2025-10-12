package com.jjimkong_backend.api.controller.post;

import com.jjimkong_backend.api.service.post.PostService;
import com.jjimkong_backend.api.service.post.dto.request.PostSaveRequest;
import com.jjimkong_backend.api.service.post.dto.request.PostUpdateRequest;
import com.jjimkong_backend.api.service.post.dto.response.PostResponse;
import com.jjimkong_backend.global.config.oauth2.CustomOAuth2User;
import com.jjimkong_backend.global.response.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Slf4j
public class PostControllerV1 {

    private final PostService postService;

    /**
     * ✅ 가게 리스트 조회
     * GET /api/v1/posts
     */
    @GetMapping
    public ApiResponse<List<PostResponse>> getPostList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("PostControllerV1.getPostList - userId: {}", customOAuth2User.getUser().getId());
        return ApiResponse.ok(postService.getPostList(customOAuth2User.getUser()));
    }

    /**
     * ✅ 가게 상세 조회
     * GET /api/v1/posts/{postId}
     */
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("PostControllerV1.getPost - userId: {}, postId: {}", customOAuth2User.getUser().getId(), postId);
        return ApiResponse.ok(postService.getPost(postId));
    }

    /**
     * ✅ 가게 생성
     * POST /api/v1/posts
     */
    @PostMapping
    public ApiResponse<Long> savePost(@RequestBody PostSaveRequest request, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("PostControllerV1.savePost - userId: {}", customOAuth2User.getUser().getId());
        return ApiResponse.ok(postService.savePost(request, customOAuth2User.getUser()));
    }

    /**
     * ✅ 가게 수정
     * PUT /api/v1/posts/{postId}
     */
    @PutMapping("/{postId}")
    public ApiResponse<Long> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest request, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("PostControllerV1.updatePost - userId: {}, postId: {}", customOAuth2User.getUser().getId(), postId);
        return ApiResponse.ok(postService.updatePost(postId, request, customOAuth2User.getUser().getId()));
    }

    /**
     * ✅ 가게 삭제
     * DELETE /api/v1/posts/{postId}
     */
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("PostControllerV1.deletePost - userId: {}, postId: {}", customOAuth2User.getUser().getId(), postId);
        postService.deletePost(postId, customOAuth2User.getUser().getId());
        return ApiResponse.noContent();
    }
}
