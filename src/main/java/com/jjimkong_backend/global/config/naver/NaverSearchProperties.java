package com.jjimkong_backend.global.config.naver;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 네이버 검색 API 설정값.
 * <p>
 * 실제 값은 환경변수로 관리한다. {@code application.yml}에서 아래와 같이 매핑한다.
 * <pre>
 * naver:
 *   search:
 *     base-url: https://openapi.naver.com
 *     client-id: ${NAVER_SEARCH_CLIENT_ID}
 *     client-secret: ${NAVER_SEARCH_CLIENT_SECRET}
 * </pre>
 */
@ConfigurationProperties(prefix = "naver.search")
public record NaverSearchProperties(
        String baseUrl,
        String clientId,
        String clientSecret
) {

    private static final String DEFAULT_BASE_URL = "https://openapi.naver.com";

    public NaverSearchProperties {
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = DEFAULT_BASE_URL;
        }
    }
}
