package com.jjimkong_backend.api.controller.user;

import com.jjimkong_backend.api.service.user.UserService;
import com.jjimkong_backend.api.service.user.dto.response.UserResponse;
import com.jjimkong_backend.global.config.oauth2.CustomOAuth2User;
import com.jjimkong_backend.global.response.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserControllerV1 {

    private final UserService userService;

    /**
     * ✅ 회원 정보 조회
     * GET /api/v1/users/me
     */
    @GetMapping("/me")
    public ApiResponse<UserResponse> getUserInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("UserControllerV1.getUserInfo - userId: {}", customOAuth2User.getUser().getId());

        UserResponse response = userService.getUserInfo(customOAuth2User.getUser());
        return ApiResponse.ok(response);
    }

    /**
     * ✅ 로그아웃
     * POST /api/v1/users/logout
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("UserControllerV1.logout - userId: {}", customOAuth2User.getUser().getId());

        userService.logout(customOAuth2User.getUser());
        return ApiResponse.noContent();
    }

    /**
     * ✅ 회원 탈퇴
     * DELETE /api/v1/users/me
     */
    @DeleteMapping("/me")
    public ApiResponse<Void> deleteUser(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("UserControllerV1.deleteUser - userId: {}", customOAuth2User.getUser().getId());

        userService.deleteUser(customOAuth2User.getUser());
        return ApiResponse.noContent();
    }
}
