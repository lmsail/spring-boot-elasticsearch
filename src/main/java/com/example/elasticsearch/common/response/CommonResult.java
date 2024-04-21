package com.example.elasticsearch.common.response;

import com.example.elasticsearch.exception.ExceptionEnum;
import com.example.elasticsearch.exception.ExceptionHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * 公共返回对象
 *
 * @author 特工007
 * @date 2022/5/6 5:18 PM
 */
@Getter
@Setter
public class CommonResult<T> {
    private long code;
    private String msg;
    private T data;

    protected CommonResult() {
    }

    protected CommonResult(long code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功返回结果 - 默认消息
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<T>(ExceptionEnum.SUCCESS.getCode(), ExceptionEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回结果 - 只有消息
     */
    public static <T> CommonResult<T> success(String message) {
        return new CommonResult<T>(ExceptionEnum.SUCCESS.getCode(), message, null);
    }

    /**
     * 成功返回结果 - 只有数据
     *
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ExceptionEnum.SUCCESS.getCode(), ExceptionEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果 - 数据 + 自定义消息
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(ExceptionEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果 - 自定义错误码
     *
     * @param errorCode 错误码
     */
    public static <T> CommonResult<T> failed(ExceptionHandler errorCode) {
        return new CommonResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static <T> CommonResult<T> failed(ExceptionHandler errorCode, String message) {
        System.out.println("errorCode2:" + errorCode);
        return new CommonResult<T>(errorCode.getCode(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(ExceptionEnum.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> CommonResult<T> failed() {
        return failed(String.valueOf(ExceptionEnum.FAILED));
    }
}
