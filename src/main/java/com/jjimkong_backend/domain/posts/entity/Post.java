package com.jjimkong_backend.domain.posts.entity;

import com.jjimkong_backend.domain.common.BaseEntity;
import com.jjimkong_backend.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("status = 'ACTIVE'")  // 소프트 삭제: 모든 조회에서 DELETED 자동 제외
@Table(name = "posts")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "restaurant_uid")
    private String restaurantUid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    public Post(String region, String detailAddress, String restaurantName, String restaurantUid, User user, Category category) {
        this.region = region;
        this.detailAddress = detailAddress;
        this.restaurantName = restaurantName;
        this.restaurantUid = restaurantUid;
        this.user = user;
        this.category = category;
    }

    public void update(String region, String detailAddress, String restaurantName, Category category) {
        this.region = region;
        this.detailAddress = detailAddress;
        this.restaurantName = restaurantName;
        this.category = category;
    }
}