package org.dao.doraemon.excel.exception;

/**
 * 业务处理内部处理异常
 *
 * @author sucf
 * @since 2025/1/8 20:09
 */
public class ExcelBizException extends RuntimeException {
    public ExcelBizException(String message) {
        super(message);
    }
}
