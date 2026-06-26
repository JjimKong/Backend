package com.jjimkong_backend.domain.reviews.entity;

import com.jjimkong_backend.domain.common.BaseEntity;
import com.jjimkong_backend.domain.posts.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

/**
 * Post(맛집) ↔ Review 연결 엔티티. 리뷰가 어떤 게시물에 대한 것인지를 나타낸다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("status = 'ACTIVE'")
@Table(name = "post_reviews")
public class PostReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    public PostReview(Post post, Review review) {
        this.post = post;
        this.review = review;
    }
}
