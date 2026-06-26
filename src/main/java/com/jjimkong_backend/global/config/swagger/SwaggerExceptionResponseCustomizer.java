package com.jjimkong_backend.global.config.swagger;

import com.jjimkong_backend.global.response.exception.ExceptionCode;
import com.jjimkong_backend.global.response.exception.annotation.SwaggerExceptionResponse;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 컨트롤러 메서드의 {@link SwaggerExceptionResponse} 를 읽어, 거기 나열된 {@link ExceptionCode}
 * 들을 Swagger 응답(에러 예시)으로 자동 추가한다.
 * <p>
 * 같은 HTTP 상태코드를 가진 에러가 여러 개면 하나의 응답 아래에 example 로 묶는다.
 * 응답 본문 형식은 ExceptionResponse(code, Status, message, data) 와 동일하게 맞춘다.
 */
@Configuration
public class SwaggerExceptionResponseCustomizer {

    private static final String JSON = "application/json";

    @Bean
    public OperationCustomizer exceptionResponseOperationCustomizer() {
        return (operation, handlerMethod) -> {
            SwaggerExceptionResponse annotation = handlerMethod.getMethodAnnotation(SwaggerExceptionResponse.class);
            if (annotation == null) {
                return operation;
            }
            ApiResponses responses = operation.getResponses();
            for (ExceptionCode code : annotation.value()) {
                addExample(responses, code);
            }
            return operation;
        };
    }

    private void addExample(ApiResponses responses, ExceptionCode code) {
        String statusCode = String.valueOf(code.getStatus().value());
        Example example = new Example()
                .summary(code.getCode())
                .value(toJson(code));

        ApiResponse response = responses.get(statusCode);
        if (response == null) {
            MediaType mediaType = new MediaType().addExamples(code.name(), example);
            Content content = new Content().addMediaType(JSON, mediaType);
            responses.addApiResponse(statusCode, new ApiResponse()
                    .description(code.getStatus().getReasonPhrase())
                    .content(content));
            return;
        }

        Content content = response.getContent();
        if (content == null) {
            content = new Content();
            response.setContent(content);
        }
        MediaType mediaType = content.get(JSON);
        if (mediaType == null) {
            mediaType = new MediaType();
            content.addMediaType(JSON, mediaType);
        }
        mediaType.addExamples(code.name(), example);
    }

    private String toJson(ExceptionCode code) {
        return """
                {
                  "code": "%s",
                  "Status": "%s",
                  "message": "%s",
                  "data": "Error"
                }""".formatted(code.getCode(), code.getStatus().name(), code.getMessage());
    }
}
