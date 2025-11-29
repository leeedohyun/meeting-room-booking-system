package com.example.demo.exception;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CoreException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final ErrorCode errorCode;
    private final LogLevel logLevel;

    private CoreException(HttpStatus httpStatus, ErrorCode errorCode, LogLevel logLevel) {
        super(errorCode.getMessage());
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.logLevel = logLevel;
    }

    public static CoreException warn(HttpStatus httpStatus, ErrorCode errorCode) {
        return new CoreException(httpStatus, errorCode, LogLevel.WARN);
    }

    public static CoreException error(HttpStatus httpStatus, ErrorCode errorCode) {
        return new CoreException(httpStatus, errorCode, LogLevel.ERROR);
    }
}
