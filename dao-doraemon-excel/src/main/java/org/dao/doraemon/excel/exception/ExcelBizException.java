package org.dao.doraemon.excel.exception;

/**
 * 业务处理内部处理异常
 *
 * @author sucf
 * @since 1.0
 */
public class ExcelBizException extends RuntimeException {
    public ExcelBizException(String message) {
        super(message);
    }
}
