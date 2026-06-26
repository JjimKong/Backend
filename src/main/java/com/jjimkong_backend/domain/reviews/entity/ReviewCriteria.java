package com.jjimkong_backend.domain.reviews.entity;

import com.jjimkong_backend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 평가 기준 (예: 맛, 분위기, 서비스). 고정 시드로 관리한다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_criteria")
public class ReviewCriteria extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_criteria_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public ReviewCriteria(String name) {
        this.name = name;
    }
}
