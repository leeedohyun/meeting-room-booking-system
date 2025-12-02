package com.example.demo.exception;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ProblemDetail> handleCoreException(CoreException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                ex.getHttpStatus(),
                ex.getMessage()
        );
        problemDetail.setTitle(ex.getErrorCode().name());
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(problemDetail);
    }
}
