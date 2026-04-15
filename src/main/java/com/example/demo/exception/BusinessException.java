package com.example.demo.exception;

/**
 * 业务逻辑异常 — HTTP 400
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
