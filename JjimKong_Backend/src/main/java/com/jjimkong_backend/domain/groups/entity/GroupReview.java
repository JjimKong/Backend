package com.jjimkong_backend.domain.groups.entity;

import com.jjimkong_backend.domain.common.BaseEntity;
import com.jjimkong_backend.domain.reviews.Review;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "group_reviews")
public class GroupReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_review_id")
    private Long id;

    @Column(name = "shared_by", nullable = false)
    private Long sharedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;
}
