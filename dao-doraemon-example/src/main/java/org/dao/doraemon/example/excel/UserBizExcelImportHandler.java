package org.dao.doraemon.example.excel;

import lombok.extern.slf4j.Slf4j;
import org.dao.doraemon.core.utils.GsonUtils;
import org.dao.doraemon.excel.annotation.ErrorImportConfiguration;
import org.dao.doraemon.excel.annotation.ExcelImport;
import org.dao.doraemon.excel.annotation.ExecutorConfiguration;
import org.dao.doraemon.excel.annotation.ImportConfiguration;
import org.dao.doraemon.excel.imported.handler.AbstractDefaultImportHandler;
import org.dao.doraemon.excel.model.ImportBatchResultModel;
import org.dao.doraemon.excel.model.ImportResultModel;
import org.dao.doraemon.excel.wrapper.DataWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * excel import demo
 *
 * @author sucf
 * @since 1.0
 */
@ExcelImport(
        code = "user",
        configuration = @ImportConfiguration(
                maxRows = 1000,
                headRow = 1,
                isCheckHand = false,
                batchProcessRows = 10,
                skipRow = {5, 9},
                executor = @ExecutorConfiguration(
                        isParallel = true
                ),
                definitionError = @ErrorImportConfiguration(
                        isGenerateErrorFile = true,
                        errorColumnName = "Error Cause",
                        errorFileName = "ExcelImportFailedReport.xlsx"
                )
        )
)
@Slf4j
public class UserBizExcelImportHandler extends AbstractDefaultImportHandler<UserEntity> {

    @Override
    public ImportResultModel checkHead(Map<Integer, String> headMap, String requestParameter) {
        log.info("headMap={}", GsonUtils.toJson(headMap));
        return ImportResultModel.success();
    }

    @Override
    public ImportResultModel process(DataWrapper<UserEntity> dataWrapper, String requestParameter) {
        log.info("dataWrapper={}", GsonUtils.toJson(dataWrapper));
        UserEntity data = dataWrapper.getData();
        if (data.getAge() == null) {
            return ImportResultModel.success();
        }

        if (data.getAge() % 3 == 1) {
            data.setAge$("这是一个标识批复值");
            return ImportResultModel.fail("error data==error data==error data==error data==error data==error data==");
        } else if (data.getAge() % 3 == 2) {
            return ImportResultModel.fail("error");
        }
        return ImportResultModel.success();
    }

    @Override
    public List<ImportBatchResultModel> batchProcess(List<DataWrapper<UserEntity>> data, String requestParameter) {
        List<ImportBatchResultModel> result = new ArrayList<>(data.size());
        for (DataWrapper<UserEntity> datum : data) {
            Integer index = datum.getIndex();
            UserEntity user = datum.getData();
            if (user.getAge() % 6 == 1) {
                user.setAge$("这是一个标识批复值");
                result.add(ImportBatchResultModel.fail(index, "batch process error"));
            } else if (user.getAge() % 3 == 2) {
                result.add(ImportBatchResultModel.fail(index, "batch process error"));
            } else {
                result.add(ImportBatchResultModel.success(index));
            }
        }
        return result;
    }
}