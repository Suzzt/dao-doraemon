package org.dao.doraemon.example.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.dao.doraemon.excel.annotation.ErrorImportConfiguration;
import org.dao.doraemon.excel.annotation.ExcelImport;
import org.dao.doraemon.excel.annotation.ImportConfiguration;
import org.dao.doraemon.excel.imported.handler.AbstractDefaultImportHandler;
import org.dao.doraemon.excel.model.ImportResultModel;

import java.util.Map;

/**
 * excel import demo
 *
 * @author sucf
 * @create_time 2024/12/29 17:08
 */
@ExcelImport(
        code = "demo",
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
public class DemoBizExcelImportHandler extends AbstractDefaultImportHandler<UserEntity> {

    @Override
    public ImportResultModel checkHead(Map<Integer, String> headMap, String requestParameter) {
        log.info("headMap={}", new Gson().toJson(headMap));
        return ImportResultModel.success();
    }

    @Override
    public ImportResultModel process(UserEntity data, String requestParameter, AnalysisContext context) {
        log.info("data={}", new Gson().toJson(data));
        if(data.getAge()==null){
            return ImportResultModel.success();
        }
        if(data.getAge()%2 ==1){
            return ImportResultModel.fail("error data");
        }
        return ImportResultModel.success();
    }
}
