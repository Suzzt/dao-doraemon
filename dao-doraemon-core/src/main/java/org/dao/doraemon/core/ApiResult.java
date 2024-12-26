package org.dao.doraemon.core;

import lombok.Data;

/**
 * http 返回结果封装类
 * @author  sucf
 * @create_time: 2024-12-26 20:09
 */
@Data
public class ApiResult<T> {
    private boolean success = false;
    private String message;
    private String code;
    private T data;
}
