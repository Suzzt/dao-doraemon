package org.dao.doraemon.example.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.dao.doraemon.excel.annotation.ErrorImportConfiguration;
import org.dao.doraemon.excel.annotation.ExcelImport;
import org.dao.doraemon.excel.annotation.ImportConfiguration;
import org.dao.doraemon.excel.imported.handler.AbstractDefaultImportHandler;
import org.dao.doraemon.excel.model.ImportResultModel;
import org.dao.doraemon.excel.wrapper.DataWrapper;

import java.util.Collections;
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
                skipRow = {},
                errorImport = @ErrorImportConfiguration(
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
        log.info("headMap={}", new Gson().toJson(headMap));
        return ImportResultModel.success();
    }

    @Override
    public ImportResultModel process(UserEntity data, String requestParameter, AnalysisContext context) {
        log.info("data={}", new Gson().toJson(data));
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
    public List<ImportResultModel> batchProcess(List<DataWrapper<UserEntity>> data, String requestParameter) {
        return Collections.emptyList();
    }
}