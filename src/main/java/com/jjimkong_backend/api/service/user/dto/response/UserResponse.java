package com.jjimkong_backend.api.service.user.dto.response;

import com.jjimkong_backend.domain.users.entity.Provider;
import com.jjimkong_backend.domain.users.entity.Role;
import com.jjimkong_backend.domain.users.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 회원 정보 응답.
 *
 * @param id             회원 ID
 * @param email          이메일
 * @param name           이름
 * @param provider       소셜 로그인 제공자
 * @param providerUserId 제공자별 사용자 식별자
 * @param role           권한
 */
@Schema(description = "회원 정보 응답")
public record UserResponse(

        @Schema(description = "회원 ID", example = "1")
        Long id,

        @Schema(description = "이메일", example = "test@jjimkong.com")
        String email,

        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "소셜 로그인 제공자", example = "GOOGLE",
                allowableValues = {"KAKAO", "GOOGLE", "NAVER"})
        Provider provider,

        @Schema(description = "제공자별 사용자 식별자", example = "test@jjimkong.comGOOGLE")
        String providerUserId,

        @Schema(description = "권한", example = "USER",
                allowableValues = {"GUEST", "USER", "ADMIN"})
        Role role
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
            user.getId(),
            user.getEmail(),
            user.getName(),
            user.getProvider(),
            user.getProviderUserId(),
            user.getRole()
        );
    }
}
