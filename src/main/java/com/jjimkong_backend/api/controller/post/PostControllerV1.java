package com.jjimkong_backend.api.controller.post;

import com.jjimkong_backend.api.service.post.PostService;
import com.jjimkong_backend.api.service.post.dto.request.PostSaveRequest;
import com.jjimkong_backend.api.service.post.dto.request.PostUpdateRequest;
import com.jjimkong_backend.api.service.post.dto.response.PostResponse;
import com.jjimkong_backend.global.config.oauth2.CustomOAuth2User;
import com.jjimkong_backend.global.response.api.ApiResponse;
import com.jjimkong_backend.global.response.exception.ExceptionCode;
import com.jjimkong_backend.global.response.exception.annotation.SwaggerExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Post", description = "맛집 게시물(Post) API")
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
    @Operation(
            summary = "내 게시물 목록 조회",
            description = """
                    로그인한 사용자가 등록한 맛집 게시물 목록을 조회한다.
                    각 항목에는 S3 이미지 URL 목록(`imageUrls`)이 포함된다.

                    **인증 필요** (Authorization: Bearer {accessToken})
                    """
    )
    @GetMapping
    public ApiResponse<List<PostResponse>> getPostList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("PostControllerV1.getPostList - userId: {}", customOAuth2User.getUser().getId());
        return ApiResponse.ok(postService.getPostList(customOAuth2User.getUser()));
    }

    /**
     * ✅ 가게 상세 조회
     * GET /api/v1/posts/{postId}
     */
    @Operation(
            summary = "게시물 상세 조회",
            description = """
                    `postId`로 단일 게시물 상세를 조회한다. 응답에 이미지 URL 목록(`imageUrls`)이 포함된다.

                    **인증 필요**
                    """
    )
    @SwaggerExceptionResponse({ExceptionCode.NOT_FOUND_POST})
    @GetMapping("/{postId}")
    public ApiResponse<PostResponse> getPost(@PathVariable Long postId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("PostControllerV1.getPost - userId: {}, postId: {}", customOAuth2User.getUser().getId(), postId);
        return ApiResponse.ok(postService.getPost(postId));
    }

    /**
     * ✅ 가게 생성 (이미지 다중 업로드)
     * POST /api/v1/posts  (multipart/form-data)
     */
    @Operation(
            summary = "게시물 생성 (이미지 다중 업로드)",
            description = """
                    맛집 게시물을 생성한다. `multipart/form-data` 로 요청한다.

                    - `request` 파트(application/json): 게시물 정보(region, detailAddress, restaurantName, restaurantUid, category)
                    - `images` 파트(선택): 이미지 파일들. 여러 장 첨부 가능하며 S3에 업로드되고 URL이 저장된다.

                    `category` 는 `RESTAURANT` | `CAFE` | `ACCOMMODATION` | `ACTIVITY` | `ETC` 중 하나.

                    **인증 필요**
                    """
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    // request 파트를 application/json 으로 전송하도록 명시(Swagger UI에서 415 방지)
                    encoding = @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE)
            )
    )
    @SwaggerExceptionResponse({ExceptionCode.INVALID_IMAGE_FILE, ExceptionCode.IMAGE_UPLOAD_FAILED})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Long> savePost(
            @RequestPart("request") PostSaveRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("PostControllerV1.savePost - userId: {}, imageCount: {}",
                customOAuth2User.getUser().getId(), images == null ? 0 : images.size());
        return ApiResponse.ok(postService.savePost(request, images, customOAuth2User.getUser()));
    }

    /**
     * ✅ 가게 수정
     * PUT /api/v1/posts/{postId}
     */
    @Operation(
            summary = "게시물 수정 (이미지 교체 가능)",
            description = """
                    게시물의 텍스트 정보(region, detailAddress, restaurantName, category)를 수정한다. `multipart/form-data` 로 요청한다.

                    - `request` 파트(application/json): 수정할 게시물 정보
                    - `images` 파트(선택):
                      - 보내면 **기존 이미지를 전부 교체**(기존은 소프트 삭제, S3 객체는 보존)
                      - 보내지 않으면 **기존 이미지 유지**(텍스트만 수정)

                    **본인 게시물만 수정 가능 · 인증 필요**
                    """
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    encoding = @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE)
            )
    )
    @SwaggerExceptionResponse({ExceptionCode.NOT_FOUND_POST, ExceptionCode.NO_PERMISSION_TO_UPDATE_POST,
            ExceptionCode.INVALID_IMAGE_FILE, ExceptionCode.IMAGE_UPLOAD_FAILED})
    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Long> updatePost(
            @PathVariable Long postId,
            @RequestPart("request") PostUpdateRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("PostControllerV1.updatePost - userId: {}, postId: {}, images: {}",
                customOAuth2User.getUser().getId(), postId,
                images == null ? "unchanged" : String.valueOf(images.size()));
        return ApiResponse.ok(postService.updatePost(postId, request, images, customOAuth2User.getUser().getId()));
    }

    /**
     * ✅ 가게 삭제
     * DELETE /api/v1/posts/{postId}
     */
    @Operation(
            summary = "게시물 삭제 (소프트 삭제)",
            description = """
                    게시물을 삭제한다. 실제 삭제가 아니라 `status=DELETED` 로 바꾸는 소프트 삭제이며,
                    연결된 이미지도 함께 소프트 삭제된다(S3 객체는 보존). **본인 게시물만 삭제 가능**.

                    **인증 필요**
                    """
    )
    @SwaggerExceptionResponse({ExceptionCode.NOT_FOUND_POST, ExceptionCode.NO_PERMISSION_TO_DELETE_POST})
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("PostControllerV1.deletePost - userId: {}, postId: {}", customOAuth2User.getUser().getId(), postId);
        postService.deletePost(postId, customOAuth2User.getUser().getId());
        return ApiResponse.noContent();
    }
}
