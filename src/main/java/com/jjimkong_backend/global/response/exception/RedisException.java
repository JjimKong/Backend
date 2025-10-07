package com.jjimkong_backend.global.response.exception;

public class RedisException extends CustomException{

    public RedisException(ExceptionCode exceptionCode) { super(exceptionCode);}
}
