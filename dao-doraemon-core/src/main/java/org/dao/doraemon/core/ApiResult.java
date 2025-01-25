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

    public ApiResult() {
    }

    public ApiResult(boolean success, String message, String code, T data) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(true, "success", "0000", data);
    }
}
