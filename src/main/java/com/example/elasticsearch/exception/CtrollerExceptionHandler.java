package com.example.elasticsearch.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * 定义全局异常类
 *
 * @author 特工007
 * @date 2022/5/6 4:08 PM
 */

@RestControllerAdvice
public class CtrollerExceptionHandler {

    /**
     * 处理系统异常,当发生数组角标越界异常的时候，会执行此方法
     *
     * @param e
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ExceptionMessage handleValidationBodyException(ArrayIndexOutOfBoundsException e) {
        e.printStackTrace();
        return new ExceptionMessage(ExceptionEnum.ARRAY_INDEX_OUT_OF_BOUND.getCode(), e.getMessage());
    }

    /**
     * 其他异常，如果发生了异常，但不属于上面的异常类别，就会执行此方法
     *
     * @param e
     * @param response
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ExceptionMessage handleAllException(Exception e, HttpServletResponse response) {
        e.printStackTrace();
        return new ExceptionMessage(ExceptionEnum.OTHER_EXCEPTION.getCode(), e.getMessage());
    }
}
