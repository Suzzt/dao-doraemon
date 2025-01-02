package org.dao.doraemon.example.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelRedBorderAndComment {
    public static void main(String[] args) throws IOException {
        // 创建工作簿和工作表
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Example");

        // 模拟错误数据
        Map<Integer, String> errorData = new HashMap<>();
        errorData.put(1, "格式错误");
        errorData.put(3, "缺少必填字段");
        errorData.put(5, "数据类型不匹配");

        // 创建一个绘图对象，用于添加注释
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

        // 创建红色边框的样式
        CellStyle redBorderStyle = workbook.createCellStyle();
        redBorderStyle.setBorderTop(BorderStyle.THIN);
        redBorderStyle.setBorderBottom(BorderStyle.THIN);
        redBorderStyle.setBorderLeft(BorderStyle.THIN);
        redBorderStyle.setBorderRight(BorderStyle.THIN);
        redBorderStyle.setTopBorderColor(IndexedColors.RED.getIndex());
        redBorderStyle.setBottomBorderColor(IndexedColors.RED.getIndex());
        redBorderStyle.setLeftBorderColor(IndexedColors.RED.getIndex());
        redBorderStyle.setRightBorderColor(IndexedColors.RED.getIndex());

        // 遍历错误数据，为对应单元格添加红色边框和注释
        for (Map.Entry<Integer, String> entry : errorData.entrySet()) {
            int rowIndex = entry.getKey();
            String errorMessage = entry.getValue();

            // 获取或创建行
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }

            // 获取或创建单元格
            Cell cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }

            // 设置单元格值
            cell.setCellValue("错误数据");

            // 应用红色边框样式
            cell.setCellStyle(redBorderStyle);

            // 创建注释
            XSSFClientAnchor anchor = new XSSFClientAnchor();
            anchor.setCol1(1);  // 起始列
            anchor.setCol2(3);  // 结束列
            anchor.setRow1(rowIndex);  // 起始行
            anchor.setRow2(rowIndex + 1);  // 结束行

            Comment comment = drawing.createCellComment(anchor);
            comment.setString(new XSSFRichTextString(errorMessage));
            comment.setAuthor("系统");

            // 将注释绑定到单元格
            cell.setCellComment(comment);
        }

        // 自动调整列宽
        sheet.autoSizeColumn(0);

        // 将工作簿写入文件
        try (FileOutputStream fileOut = new FileOutputStream("/Users/sucf/Downloads/ExcelWithComments1.xlsx")) {
            workbook.write(fileOut);
        }

        workbook.close();
        System.out.println("Excel 文件已生成，单元格已设置红色边框和注释！");
    }
}
