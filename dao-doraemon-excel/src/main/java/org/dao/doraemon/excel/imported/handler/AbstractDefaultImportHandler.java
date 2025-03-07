package org.dao.doraemon.excel.imported.handler;

import org.apache.poi.ss.usermodel.*;
import org.dao.doraemon.excel.model.ImportBatchResultModel;
import org.dao.doraemon.excel.model.ImportResultModel;
import org.dao.doraemon.excel.wrapper.DataWrapper;

import java.util.List;
import java.util.Map;

/**
 * 默认的导入处理器
 * 业务使用集成的入口类
 *
 * @author sucf
 * @since 1.0
 */
public abstract class AbstractDefaultImportHandler<T> implements ImportHandler<T> {

    @Override
    public abstract ImportResultModel checkHead(Map<Integer, String> headMap, String requestParameter);

    @Override
    public abstract ImportResultModel process(DataWrapper<T> dataWrapper, String requestParameter);

    @Override
    public abstract List<ImportBatchResultModel> batchProcess(List<DataWrapper<T>> data, String requestParameter);

    @Override
    public String defineFailFileName(String parameter) {
        return null;
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

        // 创建新样式并复制前一列的样式
        CellStyle newCellStyle = workbook.createCellStyle();
        Cell targetCopyCell = row.getCell(headColumn - 1);
        if (targetCopyCell != null) {
            CellStyle targetCellStyle = targetCopyCell.getCellStyle();
            newCellStyle.cloneStyleFrom(targetCellStyle);
        }

        // 设置字体为红色
        Font font = workbook.createFont();
        font.setColor(IndexedColors.RED.getIndex());
        newCellStyle.setFont(font);

        // 设置字体居中
        newCellStyle.setAlignment(HorizontalAlignment.CENTER);
        newCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 应用新样式到单元格
        cell.setCellStyle(newCellStyle);

        // 调整列宽以自适应内容
        sheet.autoSizeColumn(headColumn);

        int currentColumnWidth = sheet.getColumnWidth(headColumn);
        sheet.setColumnWidth(headColumn, (int) (currentColumnWidth * 1.3));
    }
}
