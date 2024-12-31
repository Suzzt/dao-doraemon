package org.dao.doraemon.excel.imported;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.handler.AbstractRowWriteHandler;
import com.alibaba.excel.write.handler.context.RowWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dao.doraemon.excel.annotation.ErrorImportConfiguration;
import org.dao.doraemon.excel.annotation.ExcelImport;
import org.dao.doraemon.excel.annotation.ImportConfiguration;
import org.dao.doraemon.excel.imported.handler.AbstractDefaultImportHandler;
import org.dao.doraemon.excel.imported.handler.ImportHandler;
import org.dao.doraemon.excel.imported.listener.ExcelProcessListener;
import org.dao.doraemon.excel.imported.resolver.GenericTypeResolver;
import org.dao.doraemon.excel.properties.ExcelImportErrorProperties;
import org.dao.doraemon.excel.properties.ExcelImportProperties;
import org.dao.doraemon.excel.server.ExcelImportResult;
import org.dao.doraemon.excel.storage.ExcelStorageProcessor;
import org.dao.doraemon.excel.wrapper.ExcelImportWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 导入文件分配器
 *
 * @author sucf
 * @create_time 2024/12/28 17:13
 */
public class Dispatcher implements BeanPostProcessor {

    /**
     * 导入Excel注册的处理器
     * key: code
     * value: properties + handler
     */
    private final Map<String, ExcelImportWrapper> resource = new HashMap<>();

    private final ExcelStorageProcessor storageProcessor;

    public Dispatcher(ExcelStorageProcessor storageProcessor) {
        this.storageProcessor = storageProcessor;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(ExcelImport.class)) {
            ExcelImport annotation = beanClass.getAnnotation(ExcelImport.class);
            String code = annotation.code();
            ExcelImportWrapper excelImportWrapper = getExcelImportWrapper(annotation, (AbstractDefaultImportHandler<?>) bean);
            resource.put(code, excelImportWrapper);
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    private static ExcelImportWrapper getExcelImportWrapper(ExcelImport annotation, ImportHandler<?> importHandler) {
        ImportConfiguration configuration = annotation.configuration();
        ErrorImportConfiguration errorImportConfiguration = configuration.errorImport();
        ExcelImportProperties properties = new ExcelImportProperties();
        properties.setMaxRows(configuration.maxRows());
        properties.setHeadRow(configuration.headRow());
        properties.setIsCheckHand(configuration.isCheckHand());
        properties.setBatchProcessRows(configuration.batchProcessRows());
        properties.setSkipRow(configuration.skipRow());
        ExcelImportErrorProperties errorProperties = new ExcelImportErrorProperties();
        errorProperties.setIsGenerateErrorFile(errorImportConfiguration.isGenerateErrorFile());
        errorProperties.setErrorFileName(errorImportConfiguration.errorFileName());
        errorProperties.setErrorColumnName(errorImportConfiguration.errorColumnName());
        properties.setExcelImportErrorProperties(errorProperties);

        ExcelImportWrapper excelImportWrapper = new ExcelImportWrapper();
        excelImportWrapper.setExcelImportProperties(properties);
        excelImportWrapper.setImportHandler(importHandler);
        return excelImportWrapper;
    }

    /**
     * excel 导入数据处理
     *
     * @param code
     * @param parameter
     * @param inputStream
     * @return
     */
    public ExcelImportResult execute(String code, String parameter, InputStream inputStream) throws IOException {
        ExcelImportWrapper excelImportWrapper = resource.get(code);
        Assert.notNull(excelImportWrapper, "not fond excel handler, check you code.");
        ExcelImportProperties excelProperties = excelImportWrapper.getExcelImportProperties();
        ImportHandler<?> importHandler = excelImportWrapper.getImportHandler();
        Class<?> bizClazz = GenericTypeResolver.getGenericType(importHandler.getClass());
        ExcelProcessListener<?> readListener = new ExcelProcessListener<>(importHandler, excelImportWrapper.getExcelImportProperties(), parameter);
        // todo 先支持第一个 sheet 吧
        EasyExcel.read(inputStream, bizClazz, readListener).headRowNumber(excelProperties.getHeadRow()).sheet().doRead();

        ExcelImportResult result = new ExcelImportResult();
        Map<Integer, String> failCollector = readListener.getFailCollector();
        int totalCount = readListener.getTotalRows();
        int failCount = failCollector.size();
        int skipCount = readListener.getSkipRows();
        result.setTotalCount(totalCount);
        result.setFailCount(failCount);
        result.setSuccessCount(totalCount - failCount);
        result.setSkipCount(skipCount);

        ExcelImportErrorProperties errorProperties = excelProperties.getExcelImportErrorProperties();
        if (failCount != 0 && errorProperties.getIsGenerateErrorFile()) {
            // 原文件数据
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // 生成错误的表头信息 追加到原有文件指定表头行的排序后最后一列
            int headRow = excelProperties.getHeadRow();
            Row row = sheet.getRow(headRow);
            int headColumn = row == null ? 0 : row.getLastCellNum();
            importHandler.generateErrorHeadStyle(workbook, sheet, headRow, headColumn, errorProperties.getErrorColumnName(), parameter);

            // 将处理失败的信息填充到原文件最后一列
            for (Map.Entry<Integer, String> entry : failCollector.entrySet()) {
                Integer failRow = entry.getKey();
                String failMessage = entry.getValue();

                Row failDataRow = sheet.getRow(failRow);
                if (failDataRow == null) {
                    failDataRow = sheet.createRow(failRow);
                }

                // 确定写入错误信息的列索引（最后一列+1）
                Cell errorCell = failDataRow.getCell(headColumn);
                if (errorCell == null) {
                    errorCell = failDataRow.createCell(headColumn);
                }

                // 获取最后一列（即错误列前一列）的样式
                CellStyle previousCellStyle = null;
                if (headColumn > 0) {
                    Cell previousCell = failDataRow.getCell(headColumn - 1);
                    if (previousCell != null) {
                        previousCellStyle = previousCell.getCellStyle();
                    }
                }

                // 复制样式到错误信息列
                if (previousCellStyle != null) {
                    errorCell.setCellStyle(previousCellStyle);
                }

                // 写入错误信息
                errorCell.setCellValue(failMessage);

                try (FileOutputStream fileOutputStream = new FileOutputStream("/Users/sucf/Downloads/00000.xlsx")) {
                    workbook.write(fileOutputStream);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to write workbook to file", e);
                } finally {
                    // 确保关闭 Workbook 以释放资源
                    workbook.close();
                }
            }
        }
        return result;
    }
}
