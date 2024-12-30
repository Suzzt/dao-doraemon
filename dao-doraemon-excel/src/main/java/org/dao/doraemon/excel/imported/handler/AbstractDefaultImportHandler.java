package org.dao.doraemon.excel.imported.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import org.dao.doraemon.excel.model.ImportResultModel;
import org.dao.doraemon.excel.properties.ExcelImportErrorProperties;
import org.dao.doraemon.excel.properties.ExcelImportProperties;

import java.util.Map;

/**
 * 默认的导入处理器
 * 业务使用集成的入口类
 *
 * @author sucf
 * @create_time 2024/12/28 17:05
 */
public abstract class AbstractDefaultImportHandler<T> implements ImportHandler<T> {

    @Override
    public abstract ImportResultModel checkHead(Map<Integer, ReadCellData<?>> headMap, String requestParameter);

    @Override
    public abstract ImportResultModel process(T data, String requestParameter,AnalysisContext context);

    @Override
    public String defineFailFileName(ExcelImportProperties excelImportProperties) {
        ExcelImportErrorProperties excelImportErrorProperties = excelImportProperties.getExcelImportErrorProperties();
        if (excelImportErrorProperties.getIsGenerateErrorFile()) {
            return excelImportErrorProperties.getErrorFileName();
        }
        return "ExcelFailedReport.xlsx";
    }
}
