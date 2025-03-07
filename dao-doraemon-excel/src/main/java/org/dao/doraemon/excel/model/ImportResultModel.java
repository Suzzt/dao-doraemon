package org.dao.doraemon.excel.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 导入处理结果
 *
 * @author sucf
 * @since 1.0
 */
@Getter
@Setter
public class ImportResultModel {

    /**
     * 提示信息
     */
    private String message;

    /**
     * 0:success
     * -1:fail
     * 1:skip
     */
    private Integer status;

    /**
     * build success result
     *
     * @return bingo
     */
    public static ImportResultModel success() {
        ImportResultModel resultModel = new ImportResultModel();
        resultModel.setStatus(0);
        return resultModel;
    }

    /**
     * build fail result
     *
     * @return error
     */
    public static ImportResultModel fail(String message) {
        ImportResultModel resultModel = new ImportResultModel();
        resultModel.setStatus(-1);
        resultModel.setMessage(message);
        return resultModel;
    }

    /**
     * build ignore skip result
     *
     * @return error
     */
    public static ImportResultModel skip() {
        ImportResultModel resultModel = new ImportResultModel();
        resultModel.setStatus(1);
        return resultModel;
    }
}
