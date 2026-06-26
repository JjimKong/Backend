package com.jjimkong_backend.api.controller.user;

import com.jjimkong_backend.api.service.user.UserService;
import com.jjimkong_backend.api.service.user.dto.response.UserResponse;
import com.jjimkong_backend.global.config.oauth2.CustomOAuth2User;
import com.jjimkong_backend.global.response.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "회원(User) API")
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
    @Operation(
            summary = "내 회원 정보 조회",
            description = """
                    로그인한 사용자의 회원 정보를 조회한다.

                    **인증 필요** (Authorization: Bearer {accessToken})
                    """
    )
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
    @Operation(
            summary = "로그아웃",
            description = """
                    로그아웃 처리한다. 서버에 저장된 RefreshToken을 제거한다.

                    **인증 필요**
                    """
    )
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
    @Operation(
            summary = "회원 탈퇴",
            description = """
                    로그인한 사용자를 탈퇴 처리한다.

                    **인증 필요**
                    """
    )
    @DeleteMapping("/me")
    public ApiResponse<Void> deleteUser(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        log.info("UserControllerV1.deleteUser - userId: {}", customOAuth2User.getUser().getId());

        userService.deleteUser(customOAuth2User.getUser());
        return ApiResponse.noContent();
    }
}
