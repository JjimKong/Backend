package com.jjimkong_backend.domain.posts.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    RESTAURANT("식당"),
    CAFE("카페"),
    ACCOMMODATION("숙소"),
    ACTIVITY("액티비티"),
    ETC("기타");

    private final String description;
}
