package org.dao.doraemon.excel.model;


import lombok.Getter;
import lombok.Setter;

/**
 * 批量导入处理结果
 *
 * @author sucf
 * @since 1.0
 */
@Setter
@Getter
public class ImportBatchResultModel extends ImportResultModel {
    private Integer index;

    /**
     * build success result
     *
     * @return bingo
     */
    public static ImportBatchResultModel success(Integer index) {
        ImportBatchResultModel resultModel = new ImportBatchResultModel();
        resultModel.setStatus(0);
        resultModel.setIndex(index);
        return resultModel;
    }

    /**
     * build fail result
     *
     * @return error
     */
    public static ImportBatchResultModel fail(Integer index, String message) {
        ImportBatchResultModel resultModel = new ImportBatchResultModel();
        resultModel.setStatus(-1);
        resultModel.setMessage(message);
        resultModel.setIndex(index);
        return resultModel;
    }

    /**
     * build ignore skip result
     *
     * @return error
     */
    public static ImportResultModel skip(Integer index) {
        ImportBatchResultModel resultModel = new ImportBatchResultModel();
        resultModel.setStatus(1);
        resultModel.setIndex(index);
        return resultModel;
    }
}
