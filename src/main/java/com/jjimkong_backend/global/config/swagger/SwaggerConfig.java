package com.jjimkong_backend.global.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc OpenAPI 설정.
 * <p>
 * JWT AccessToken을 사용하는 Bearer 인증 스킴을 정의해, Swagger UI의
 * "Authorize" 버튼에서 {@code Authorization: Bearer {accessToken}} 헤더를 넣어
 * 인증이 필요한 API를 바로 호출할 수 있게 한다.
 */
@Configuration
public class SwaggerConfig {

    /** 컨트롤러의 @SecurityRequirement(name = ...) 와 일치해야 한다. */
    public static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JjimKong API")
                        .description("JjimKong 백엔드 REST API 문서")
                        .version("v1"))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("OAuth2 로그인으로 발급받은 AccessToken을 입력한다.")));
    }
}
