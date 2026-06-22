package com.jjimkong_backend.global.config.naver;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * 네이버 검색 API 호출 전용 {@link RestClient} 빈 설정.
 * <p>
 * 인증 헤더({@code X-Naver-Client-Id}, {@code X-Naver-Client-Secret})를
 * 기본 헤더로 주입해 둔다.
 */
@Configuration
@EnableConfigurationProperties(NaverSearchProperties.class)
@RequiredArgsConstructor
public class NaverSearchClientConfig {

    private final NaverSearchProperties properties;

    @Bean
    public RestClient naverSearchRestClient() {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader("X-Naver-Client-Id", properties.clientId())
                .defaultHeader("X-Naver-Client-Secret", properties.clientSecret())
                .build();
    }
}
