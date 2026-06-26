package com.jjimkong_backend.global.response.exception;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ExceptionCode {

    // Post
    NOT_FOUND_POST("E_PST_001", NOT_FOUND, "해당하는 Post가 없습니다."),
    NOT_FOUND_USER("E_PST_002", NOT_FOUND, "해당하는 User가 없습니다."),
    NO_PERMISSION_TO_UPDATE_POST("E_PST_003", FORBIDDEN, "Post 수정 권한이 없습니다."),
    NO_PERMISSION_TO_DELETE_POST("E_PST_004", FORBIDDEN, "Post 삭제 권한이 없습니다."),

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
    REDIS_DATA_DELETE_ERROR("E_REDIS", BAD_REQUEST, "Redis에 저장된 데이터 삭제 중 오류가 발생했습니다."),

    // Map (네이버 지도/검색)
    NAVER_SEARCH_API_ERROR("E_MAP_001", BAD_GATEWAY, "네이버 검색 API 호출 중 오류가 발생했습니다."),

    // Image (S3)
    IMAGE_UPLOAD_FAILED("E_IMG_001", HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다."),
    INVALID_IMAGE_FILE("E_IMG_002", BAD_REQUEST, "유효하지 않은 이미지 파일입니다."),

    // Review
    NOT_FOUND_REVIEW("E_RVW_001", NOT_FOUND, "해당하는 리뷰가 없습니다."),
    NO_PERMISSION_TO_UPDATE_REVIEW("E_RVW_002", FORBIDDEN, "리뷰 수정 권한이 없습니다."),
    NO_PERMISSION_TO_DELETE_REVIEW("E_RVW_003", FORBIDDEN, "리뷰 삭제 권한이 없습니다."),
    NOT_FOUND_REVIEW_CRITERIA("E_RVW_004", NOT_FOUND, "해당하는 평가 기준이 없습니다.");

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
