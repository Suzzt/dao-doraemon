package org.dao.doraemon.example.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelCommentExample {
    public static void main(String[] args) throws IOException {
        // 创建一个工作簿和工作表
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Example");

        // 创建一个单元格
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("错误数据");

        // 创建一个绘图对象，用于添加注释
        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

        // 定义注释的位置和大小
        XSSFClientAnchor anchor = new XSSFClientAnchor();
        anchor.setCol1(1);  // 起始列
        anchor.setCol2(3);  // 结束列
        anchor.setRow1(0);  // 起始行
        anchor.setRow2(2);  // 结束行

        // 创建注释对象
        Comment comment = drawing.createCellComment(anchor);
        comment.setString(new XSSFRichTextString("这是一个错误数据，请检查格式。"));
        comment.setAuthor("系统");

        // 将注释添加到单元格
        cell.setCellComment(comment);

        // 将工作簿写入文件
        try (FileOutputStream fileOut = new FileOutputStream("/Users/sucf/Downloads/ExcelWithComments.xlsx")) {
            workbook.write(fileOut);
        }

        workbook.close();
        System.out.println("Excel 文件已创建，并添加了注释！");
    }
}
