package com.jjimkong_backend.api.controller.dev;

import com.jjimkong_backend.domain.users.entity.Provider;
import com.jjimkong_backend.domain.users.entity.Role;
import com.jjimkong_backend.domain.users.entity.User;
import com.jjimkong_backend.domain.users.repository.UserRepository;
import com.jjimkong_backend.global.config.jwt.service.JwtService;
import com.jjimkong_backend.global.response.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * [dev 전용] 테스트용 AccessToken 발급.
 * OAuth 로그인 없이 보호된 API를 테스트하기 위한 용도다.
 * {@code @Profile("dev")} 라 운영(prod)에는 이 빈이 등록되지 않아 엔드포인트 자체가 존재하지 않는다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dev")
@Profile("dev")
@Slf4j
public class DevTokenController {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    /**
     * 테스트 토큰 발급 (해당 providerUserId 유저가 없으면 자동 생성)
     * GET /api/v1/dev/token?email=test@jjimkong.com&provider=GOOGLE
     * 응답으로 받은 토큰을 Swagger 우상단 Authorize 에 넣어 사용한다.
     */
    @GetMapping("/token")
    public ApiResponse<String> issueTestToken(
            @RequestParam(defaultValue = "test@jjimkong.com") String email,
            @RequestParam(defaultValue = "GOOGLE") String provider,
            @RequestParam(defaultValue = "USER") String role) {

        // 토큰 클레임 providerUserId = email + provider 와 동일하게 맞춰야 JwtAuthFilter가 유저를 찾는다.
        // ADMIN 권한 테스트 시: ?email=admin@jjimkong.com&role=ADMIN 처럼 다른 email로 새 유저를 만든다.
        String providerUserId = email + provider;
        User user = userRepository.findByProviderUserId(providerUserId)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .name("테스트유저")
                                .email(email)
                                .provider(Provider.valueOf(provider))
                                .providerUserId(providerUserId)
                                .role(Role.valueOf(role))
                                .build()));

        String accessToken = jwtService.createAccessToken(email, provider);
        log.info("DevTokenController.issueTestToken - userId: {}, providerUserId: {}", user.getId(), providerUserId);
        return ApiResponse.ok(accessToken);
    }
}
