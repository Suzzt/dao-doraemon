package org.dao.doraemon.excel.imported.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import org.apache.poi.ss.usermodel.*;
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
    public abstract ImportResultModel checkHead(Map<Integer, String> headMap, String requestParameter);

    @Override
    public abstract ImportResultModel process(T data, String requestParameter, AnalysisContext context);

    @Override
    public String defineFailFileName(ExcelImportProperties excelImportProperties) {
        ExcelImportErrorProperties excelImportErrorProperties = excelImportProperties.getExcelImportErrorProperties();
        if (excelImportErrorProperties.getIsGenerateErrorFile()) {
            return excelImportErrorProperties.getErrorFileName();
        }
        return "ExcelFailedReport.xlsx";
    }

    @Override
    public void generateErrorHeadStyle(Workbook workbook, Sheet sheet, int headRow, int headColumn, String headTitle, String parameter) {
        Row row = sheet.getRow(headRow);
        if (row == null) {
            row = sheet.createRow(headRow);
        }
        Cell cell = row.getCell(headColumn);
        if (cell == null) {
            cell = row.createCell(headColumn);
        }
        cell.setCellValue(headTitle);
        // 复制前一行那列的表头样式
        CellStyle newCellStyle = workbook.createCellStyle();
        Cell targetCopyCell = row.getCell(headColumn - 1);
        if (targetCopyCell != null) {
            CellStyle targetCellStyle = targetCopyCell.getCellStyle();
            newCellStyle.cloneStyleFrom(targetCellStyle);
            cell.setCellStyle(newCellStyle);
        }
    }
}
