package com.jjimkong_backend.domain.posts.entity;

import com.jjimkong_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("status = 'ACTIVE'")  // 소프트 삭제: 조회 시 DELETED 자동 제외
@Table(name = "post_images")
public class PostImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // S3에 접근하는 공개 URL
    @Column(name = "image_url", nullable = false, length = 512)
    private String imageUrl;

    // 버킷 내 객체 키 (삭제 시 사용)
    @Column(name = "s3_key", nullable = false, length = 512)
    private String s3Key;

    public PostImage(Post post, String imageUrl, String s3Key) {
        this.post = post;
        this.imageUrl = imageUrl;
        this.s3Key = s3Key;
    }
}
