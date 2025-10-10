package com.jjimkong_backend.api.service.user.dto.response;

import com.jjimkong_backend.domain.users.entity.User;

public record UserResponse(
    Long id,
    String email,
    String name
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getName()
        );
    }
}
