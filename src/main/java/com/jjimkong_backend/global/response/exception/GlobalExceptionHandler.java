package com.jjimkong_backend.global.response.exception;

import com.jjimkong_backend.global.response.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    protected ApiResponse<Object> bindException(BindException e) {

        log.error("BindException : {}", e.getMessage());

        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                "Error",
                HttpStatus.BAD_REQUEST.name()
        );
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(CustomException e){
        ExceptionCode exceptionCode = e.getExceptionCode();

        return ResponseEntity.status(exceptionCode.getStatus())
                .body(ExceptionResponse.from(exceptionCode));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        ExceptionCode exceptionCode =  ExceptionCode.INTERNAL_SERVER_ERROR;
        exceptionCode.updateServerErrorMessage(e.getMessage());

        return ResponseEntity.status(exceptionCode.getStatus())
                .body(ExceptionResponse.from(exceptionCode));
    }


}
