package org.dao.doraemon.excel.imported;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.excel.EasyExcel;
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
     * @param code        code
     * @param parameter   请求参数
     * @param inputStream 导入文件流
     */
    public ExcelImportResult execute(String code, String parameter, InputStream inputStream) throws IOException {
        ExcelImportWrapper excelImportWrapper = resource.get(code);
        Assert.notNull(excelImportWrapper, "not fond excel handler, check you code.");
        ExcelImportProperties excelProperties = excelImportWrapper.getExcelImportProperties();
        ImportHandler<?> importHandler = excelImportWrapper.getImportHandler();
        Class<?> bizClazz = GenericTypeResolver.getGenericType(importHandler.getClass());
        ExcelProcessListener<?> readListener = new ExcelProcessListener<>(importHandler, excelProperties, parameter);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
        } finally {
            inputStream.close();
        }

        // 转为可重复使用的流
        byte[] excelData = byteArrayOutputStream.toByteArray();
        InputStream reusableInputStream1 = new ByteArrayInputStream(excelData);
        InputStream reusableInputStream2 = new ByteArrayInputStream(excelData);

        ExcelImportResult result = new ExcelImportResult();

        try {
            // 使用 EasyExcel 读取数据
            EasyExcel.read(reusableInputStream1, bizClazz, readListener).headRowNumber(excelProperties.getHeadRow()).sheet().doRead();

            // 收集读取结果
            Map<Integer, String> failCollector = readListener.getFailCollector();
            int totalCount = readListener.getTotalRows();
            int failCount = failCollector.size();
            int skipCount = readListener.getSkipRows();
            result.setTotalCount(totalCount);
            result.setFailCount(failCount);
            result.setSuccessCount(totalCount - failCount);
            result.setSkipCount(skipCount);

            // 如果需要生成错误文件
            ExcelImportErrorProperties errorProperties = excelProperties.getExcelImportErrorProperties();
            if (failCount != 0 && errorProperties.getIsGenerateErrorFile()) {
                try (Workbook workbook = new XSSFWorkbook(reusableInputStream2)) {
                    Sheet sheet = workbook.getSheetAt(0);

                    // 生成错误的表头信息追加到原有文件最后一列
                    int headRow = excelProperties.getHeadRow() - 1;
                    Row row = sheet.getRow(headRow);
                    int headColumn = row == null ? 0 : row.getLastCellNum();
                    importHandler.generateErrorHeadStyle(workbook, sheet, headRow, headColumn, errorProperties.getErrorColumnName(), parameter);

                    // 填充错误信息
                    for (Map.Entry<Integer, String> entry : failCollector.entrySet()) {
                        Integer failRow = entry.getKey();
                        String failMessage = entry.getValue();

                        Row failDataRow = sheet.getRow(failRow);
                        if (failDataRow == null) {
                            failDataRow = sheet.createRow(failRow);
                        }

                        // 确定写入错误信息的列索引
                        Cell errorCell = failDataRow.getCell(headColumn);
                        if (errorCell == null) {
                            errorCell = failDataRow.createCell(headColumn);
                        }

                        // 写入错误信息
                        errorCell.setCellValue(failMessage);
                    }
                    // todo 怎么样把整个错误列用红色边框
                    String downloadUrl = storageProcessor.submit(new Snowflake(1, 1).nextId() + File.separator + errorProperties.getErrorFileName(), workbook);
                    result.setFailDownloadAddress(downloadUrl);
                }
            }
        } finally {
            reusableInputStream1.close();
            reusableInputStream2.close();
            byteArrayOutputStream.close();
        }

        return result;
    }

    /**
     * 下载文件
     *
     * @param path 文件路径
     * @return excel 文件流
     */
    public InputStream download(String path) {
        return storageProcessor.download(path);
    }
}
