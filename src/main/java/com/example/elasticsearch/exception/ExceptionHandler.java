package com.example.elasticsearch.exception;

import org.springframework.http.HttpStatus;

/**
 * 自定义异常类
 *
 * @author 特工007
 * @date 2022/5/6 5:00 PM
 */
public interface ExceptionHandler {

    long getCode();

    String getMessage();
}