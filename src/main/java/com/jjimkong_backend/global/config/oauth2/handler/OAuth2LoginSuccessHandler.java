package com.jjimkong_backend.global.config.oauth2.handler;

import com.jjimkong_backend.domain.users.entity.User;
import com.jjimkong_backend.global.config.jwt.service.JwtService;
import com.jjimkong_backend.global.config.oauth2.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2LoginSuccessHandler.onAuthenticationSuccess() 실행 - OAuth2 로그인 성공 요청 진입");

        try {
            User user = ((CustomOAuth2User) authentication.getPrincipal()).getUser();
            String accessToken = jwtService.createAccessToken(user.getEmail(), user.getProvider().name());

            // 로그인 성공 후 토큰 발급 및 리디렉션 처리
            handleUserLogin(response, accessToken, user.getProviderUserId());

        } catch (Exception e) {
            log.error("OAuth2 로그인 처리 중 오류 발생: ", e);
            throw e;
        }
    }

    private void handleUserLogin(HttpServletResponse response, String accessToken, String getProviderUserId) throws IOException {
        log.info("USER 상태 - 로그인 성공 처리");

        String refreshToken = jwtService.createRefreshToken(getProviderUserId);
        jwtService.saveRefreshTokenToRedis(getProviderUserId, refreshToken);
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        // 사용자가 로그인 성공 시 홈 페이지로 리디렉션
        response.sendRedirect("https://ddang.site.dev/?accessToken=" + accessToken);
    }
}

