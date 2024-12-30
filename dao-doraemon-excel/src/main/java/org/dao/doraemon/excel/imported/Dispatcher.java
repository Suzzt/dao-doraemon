package org.dao.doraemon.excel.imported;

import com.alibaba.excel.EasyExcel;
import org.dao.doraemon.excel.annotation.ErrorImportConfiguration;
import org.dao.doraemon.excel.annotation.ExcelImport;
import org.dao.doraemon.excel.annotation.ImportConfiguration;
import org.dao.doraemon.excel.imported.handler.AbstractDefaultImportHandler;
import org.dao.doraemon.excel.imported.listener.ExcelProcessListener;
import org.dao.doraemon.excel.imported.resolver.GenericTypeResolver;
import org.dao.doraemon.excel.properties.ExcelImportErrorProperties;
import org.dao.doraemon.excel.properties.ExcelImportProperties;
import org.dao.doraemon.excel.server.ExcelImportResult;
import org.dao.doraemon.excel.storage.ExcelStorageProcessor;
import org.dao.doraemon.excel.wrapper.ExcelImportWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

import java.io.InputStream;
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

    private static ExcelImportWrapper getExcelImportWrapper(ExcelImport annotation, AbstractDefaultImportHandler<?> abstractDefaultImportHandler) {
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
        excelImportWrapper.setAbstractDefaultImportHandler(abstractDefaultImportHandler);
        return excelImportWrapper;
    }

    /**
     * excel 导入数据处理
     *
     * @param code
     * @param param
     * @param inputStream
     * @return
     */
    public ExcelImportResult execute(String code, String param, InputStream inputStream) {
        ExcelImportWrapper excelImportWrapper = resource.get(code);
        AbstractDefaultImportHandler<?> abstractDefaultImportHandler = excelImportWrapper.getAbstractDefaultImportHandler();
        Class<?> bizClazz = GenericTypeResolver.getGenericType(abstractDefaultImportHandler.getClass());
        ExcelProcessListener<?> readListener = new ExcelProcessListener<>(abstractDefaultImportHandler, excelImportWrapper.getExcelImportProperties(), param);
        EasyExcel.read(inputStream, bizClazz, readListener).sheet().doRead();
        Map<Integer, String> failCollector = readListener.getFailCollector();
        int totalCount = readListener.getTotalRows();
        // build result
        ExcelImportResult result = new ExcelImportResult();
        int failCount = failCollector.size();
        result.setTotalCount(totalCount);
        result.setFailCount(failCount);
        result.setSuccessCount(totalCount - failCount);
        if (failCount != 0) {
            // todo 构建错误文件
            result.setFailDownloadAddress("http://localhost:8080/download/fail" + code + ".xlsx");
        }
        return result;
    }
}
