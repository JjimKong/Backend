package com.jjimkong_backend.domain.reviews.entity;

import com.jjimkong_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

/**
 * 리뷰의 평가 기준별 점수.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("status = 'ACTIVE'")
@Table(name = "review_scores")
public class ReviewScore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_score_id")
    private Long id;

    @Column(name = "score", precision = 2)
    private Double score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criterion_id", nullable = false)
    private ReviewCriteria reviewCriteria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    public ReviewScore(Double score, ReviewCriteria reviewCriteria, Review review) {
        this.score = score;
        this.reviewCriteria = reviewCriteria;
        this.review = review;
    }
}
