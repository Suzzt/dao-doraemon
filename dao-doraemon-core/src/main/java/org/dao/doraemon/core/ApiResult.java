package org.dao.doraemon.core;

import lombok.Data;

/**
 * http 返回结果封装类
 *
 * @author sucf
 * @since 1.0
 */
@Data
public class ApiResult<T> {
    private boolean success = false;
    private String message;
    private String code;
    private T data;
}
