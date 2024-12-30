package org.dao.doraemon.excel.model;

import lombok.Data;

/**
 * 导入处理结果
 *
 * @author sucf
 * @create_time 2024/12/28 16:45
 */
@Data
public class ImportResultModel {
    private String message;
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
