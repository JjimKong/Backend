package com.jjimkong_backend.global.response.exception;

public class BadRequestException extends CustomException{

    public BadRequestException(ExceptionCode exceptionCode) { super(exceptionCode);}
}
