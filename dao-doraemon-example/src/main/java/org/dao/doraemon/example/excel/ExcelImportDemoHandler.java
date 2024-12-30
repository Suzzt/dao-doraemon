package org.dao.doraemon.example.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.dao.doraemon.excel.annotation.ExcelImport;
import org.dao.doraemon.excel.imported.handler.AbstractDefaultImportHandler;
import org.dao.doraemon.excel.model.ImportResultModel;

import java.util.Map;

/**
 * excel import demo
 *
 * @author sucf
 * @create_time 2024/12/29 17:08
 */
@ExcelImport(code = "demo")
@Slf4j
public class ExcelImportDemoHandler extends AbstractDefaultImportHandler<UserEntity> {


    @Override
    public ImportResultModel checkHead(Map<Integer, ReadCellData<?>> headMap, String requestParameter) {
        log.info("headMap={}", new Gson().toJson(headMap));
        return ImportResultModel.success();
    }

    @Override
    public ImportResultModel process(UserEntity data, String requestParameter, AnalysisContext context) {
        log.info("data={}", new Gson().toJson(data));
        return ImportResultModel.success();
    }
}
