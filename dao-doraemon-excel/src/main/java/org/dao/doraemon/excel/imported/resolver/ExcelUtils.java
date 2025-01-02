package org.dao.doraemon.excel.imported.resolver;

import org.apache.poi.ss.usermodel.*;
import org.dao.doraemon.excel.model.CommentModel;

import java.util.List;
import java.util.Map;

/**
 * Excel 工具类
 *
 * @author sucf
 * @create_time 2025/1/1 18:15
 */
public class ExcelUtils {

    /**
     * 写入错误信息的列
     *
     * @param workbook      workbook
     * @param sheet         sheet
     * @param failCollector 错误信息(key=row index, value=error message)
     * @param writeColumn   写入错误信息所在的列索引
     */
    public static void writeErrorMessage(Workbook workbook, Sheet sheet, Map<Integer, String> failCollector, int writeColumn) {
        CellStyle redBorderStyle = createRedBorderStyle(workbook);
        // 填充错误信息
        for (Map.Entry<Integer, String> entry : failCollector.entrySet()) {
            Integer failRow = entry.getKey();
            String failMessage = entry.getValue();

            Row failDataRow = sheet.getRow(failRow);
            if (failDataRow == null) {
                failDataRow = sheet.createRow(failRow);
            }

            // 确定写入错误信息的列索引
            Cell errorCell = failDataRow.getCell(writeColumn);
            if (errorCell == null) {
                errorCell = failDataRow.createCell(writeColumn);
            }

            // 写入错误信息
            errorCell.setCellValue(failMessage);

            // 设置红色边框样式
            errorCell.setCellStyle(redBorderStyle);
        }
    }

    /**
     * 填充标识错误信息
     *
     * @param workbook workbook
     * @param sheet    sheet
     * @param comments 标识信息
     */
    public static void populateErrorComment(Workbook workbook, Sheet sheet, List<CommentModel> comments) {

    }


    /**
     * 创建红色边框样式的单元格
     *
     * @param workbook
     * @return
     */
    private static CellStyle createRedBorderStyle(Workbook workbook) {
        CellStyle redBorderStyle = workbook.createCellStyle();
        redBorderStyle.setBorderTop(BorderStyle.THIN);
        redBorderStyle.setBorderBottom(BorderStyle.THIN);
        redBorderStyle.setBorderLeft(BorderStyle.THIN);
        redBorderStyle.setBorderRight(BorderStyle.THIN);
        redBorderStyle.setTopBorderColor(IndexedColors.RED.getIndex());
        redBorderStyle.setBottomBorderColor(IndexedColors.RED.getIndex());
        redBorderStyle.setLeftBorderColor(IndexedColors.RED.getIndex());
        redBorderStyle.setRightBorderColor(IndexedColors.RED.getIndex());
        return redBorderStyle;
    }
}
