package com.jjimkong_backend.global.response.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ExceptionCode {

    // 400 에러

    // 401 에러
    UNAUTHORIZED_ATK_ERROR("E_UAT", UNAUTHORIZED, "AccessToken is invalid"),
    UNAUTHORIZED_RTK_ERROR("E_URT", UNAUTHORIZED, "RefreshToken is invalid"),
    EXPIRED_TOKEN_ERROR("E_EXT", UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    UNSUPPORTED_TOKEN_ERROR("E_UST", UNAUTHORIZED, "지원하지 않는 JWT 토큰입니다."),
    EMPTY_TOKEN_ERROR("E_EMT", UNAUTHORIZED, "JWT 토큰이 비어 있습니다."),

    // 500 에러
    INTERNAL_SERVER_ERROR("E_SYS", HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다."),

    // Redis
    REDIS_DATA_SIZE_EXCEEDED_ERROR("E_REDIS", BAD_REQUEST, "Redis에 저장할 데이터 크기가 허용치를 초과했습니다."),
    REDIS_DATA_DELETE_ERROR("E_REDIS", BAD_REQUEST, "Redis에 저장된 데이터 삭제 중 오류가 발생했습니다.");

    private final String code;
    private String message;
    private final HttpStatus status;

    ExceptionCode(String code, HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void updateServerErrorMessage(String message){
        this.message = message;
    }
}
