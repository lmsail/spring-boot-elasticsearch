package com.example.elasticsearch.exception;

import java.io.Serializable;

/**
 * 异常枚举类
 *
 * @author 特工007
 * @date 2022/5/6 4:59 PM
 */
public enum ExceptionEnum implements Serializable {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    ARRAY_INDEX_OUT_OF_BOUND(200001, "数组角标越界异常"),
    NULL_POINTER_EXCEPTION(200002, "空指针异常"),
    SQL_EXCEPTION(200010, "sql语句查询异常"),
    OTHER_EXCEPTION(200030, "其他异常");

    private Integer excepCode; // 异常代码

    private String excepMessage; // 异常信息

    ExceptionEnum() { }

    ExceptionEnum(Integer excepCode, String excepMessage) {
        this.excepCode = excepCode;
        this.excepMessage = excepMessage;
    }

    public Integer getCode() {
        return excepCode;
    }

    public void setCode(Integer excepCode) {
        this.excepCode = excepCode;
    }

    public String getMessage() {
        return excepMessage;
    }

    public void setMessage(String excepMessage) {
        this.excepMessage = excepMessage;
    }
}
