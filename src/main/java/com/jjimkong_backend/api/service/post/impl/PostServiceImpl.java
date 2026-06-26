package com.jjimkong_backend.api.service.post.impl;

import com.jjimkong_backend.api.service.post.PostService;
import com.jjimkong_backend.api.service.post.dto.request.PostSaveRequest;
import com.jjimkong_backend.api.service.post.dto.request.PostUpdateRequest;
import com.jjimkong_backend.api.service.post.dto.response.PostResponse;
import com.jjimkong_backend.domain.posts.entity.Post;
import com.jjimkong_backend.domain.posts.entity.PostImage;
import com.jjimkong_backend.domain.posts.repository.PostImageRepository;
import com.jjimkong_backend.domain.posts.repository.PostRepository;
import com.jjimkong_backend.domain.users.entity.User;
import com.jjimkong_backend.global.config.s3.S3Service;
import com.jjimkong_backend.global.response.exception.BadRequestException;
import com.jjimkong_backend.global.response.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private static final String POST_IMAGE_DIR = "posts";

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final S3Service s3Service;

    @Override
    public List<PostResponse> getPostList(User user) {
        return postRepository.findByUser(user).stream()
                .map(post -> PostResponse.from(post, imageUrlsOf(post.getId())))
                .toList();
    }

    @Override
    public PostResponse getPost(Long postId) {
        Post post = findPostById(postId);

        return PostResponse.from(post, imageUrlsOf(postId));
    }

    @Override
    @Transactional
    public Long savePost(PostSaveRequest request, List<MultipartFile> images, User user) {

        Post post = postRepository.save(request.toEntity(user));
        uploadImages(post, images);
        return post.getId();
    }

    @Override
    @Transactional
    public Long updatePost(Long postId, PostUpdateRequest request, List<MultipartFile> images, Long userId) {
        Post post = findPostById(postId);
        if (!post.getUser().getId().equals(userId)) {
            throw new BadRequestException(ExceptionCode.NO_PERMISSION_TO_UPDATE_POST);
        }

        post.update(request.region(), request.detailAddress(), request.restaurantName(), request.category());

        // images가 전달된 경우에만 기존 이미지를 전부 교체(소프트 삭제 후 새로 업로드). null이면 기존 유지.
        if (images != null) {
            postImageRepository.findByPostId(postId).forEach(PostImage::delete);
            uploadImages(post, images);
        }
        return postId;
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = findPostById(postId);
        if (!post.getUser().getId().equals(userId)) {
            throw new BadRequestException(ExceptionCode.NO_PERMISSION_TO_DELETE_POST);
        }
        // 소프트 삭제 일관성: S3 객체는 보존하고 DB만 status=DELETED 로 변경(복구 가능)
        postImageRepository.findByPostId(postId).forEach(PostImage::delete);
        post.delete();
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_POST));
    }

    private List<String> imageUrlsOf(Long postId) {
        return postImageRepository.findByPostId(postId).stream()
                .map(PostImage::getImageUrl)
                .toList();
    }

    private void uploadImages(Post post, List<MultipartFile> images) {
        if (images == null) {
            return;
        }
        for (MultipartFile image : images) {
            if (image == null || image.isEmpty()) {
                continue;
            }
            S3Service.UploadResult result = s3Service.upload(image, POST_IMAGE_DIR);
            postImageRepository.save(new PostImage(post, result.url(), result.key()));
        }
    }

}