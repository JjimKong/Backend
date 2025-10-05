package com.jjimkong_backend.domain.posts.entity;

import com.jjimkong_backend.domain.category.entity.Category;
import com.jjimkong_backend.domain.common.BaseEntity;
import com.jjimkong_backend.domain.users.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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
    @JoinColumn(name = "user_id2", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}