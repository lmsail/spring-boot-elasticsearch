package com.example.elasticsearch.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 异常信息类
 *
 * @author 特工007
 * @date 2022/5/6 4:58 PM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionMessage implements Serializable {
    private static final long serialVersionUID = 8065583911104112360L;
    private Integer code; // 异常状态码
    private String msg;  // 异常消息内容
}
