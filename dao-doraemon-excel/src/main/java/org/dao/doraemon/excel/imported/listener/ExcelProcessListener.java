package org.dao.doraemon.excel.imported.listener;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;
import org.dao.doraemon.excel.exception.ExcelHeaderMismatchException;
import org.dao.doraemon.excel.exception.ExcelMarkException;
import org.dao.doraemon.excel.imported.handler.ImportHandler;
import org.dao.doraemon.excel.model.CommentModel;
import org.dao.doraemon.excel.model.ImportBatchResultModel;
import org.dao.doraemon.excel.model.ImportResultModel;
import org.dao.doraemon.excel.properties.ExcelImportProperties;
import org.dao.doraemon.excel.wrapper.DataWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Excel处理监听
 *
 * @author sucf
 * @since 1.0
 */
public class ExcelProcessListener<T> extends AnalysisEventListener<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelProcessListener.class);

    private final ImportHandler<T> handler;
    private final ExcelImportProperties excelImportProperties;
    private final String requestParameter;
    private final BitSet skipRowBitSet;

    private final List<DataWrapper<T>> dataCollector = new ArrayList<>();
    @Getter
    private final Map<Integer, String> failCollector = new HashMap<>();
    @Getter
    private final List<CommentModel> commentCollector = new ArrayList<>();

    @Getter
    private int totalRows = 0;
    @Getter
    private int skipRows = 0;
    private Map<String, Integer> fieldMapper;

    public ExcelProcessListener(ImportHandler<T> handler, ExcelImportProperties excelImportProperties, String requestParameter) {
        this.handler = handler;
        this.excelImportProperties = excelImportProperties;
        this.requestParameter = requestParameter;

        // 初始化跳过的行
        skipRowBitSet = new BitSet();
        for (int row : excelImportProperties.getSkipRow()) {
            skipRowBitSet.set(row);
        }
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        int rowIndex = context.readRowHolder().getRowIndex();
        totalRows++;

        if (skipRowBitSet.get(rowIndex)) {
            skipRows++;
            return;
        }

        dataCollector.add(new DataWrapper<>(rowIndex, data));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (totalRows > excelImportProperties.getMaxRows()) {
            throw new ExcelMarkException("文件中的数量超过设置的最大值 (" + excelImportProperties.getMaxRows() + ")");
        }

        int batchProcessRows = excelImportProperties.getBatchProcessRows();
        if (batchProcessRows > 1) {
            List<List<DataWrapper<T>>> splitData = CollUtil.split(dataCollector, batchProcessRows);
            splitData.forEach(this::processBatch);
        } else {
            dataCollector.forEach(this::processOne);
        }
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        if (fieldMapper == null) {
            fieldMapper = new HashMap<>();
            headMap.forEach((key, value) -> fieldMapper.put(value, key));
        }

        int rowIndex = context.readRowHolder().getRowIndex();
        if (excelImportProperties.getIsCheckHand() && Objects.equals(rowIndex, excelImportProperties.getHeadRow())) {
            ImportResultModel headResult = handler.checkHead(headMap, requestParameter);
            if (headResult.getStatus() == -1) {
                throw new ExcelHeaderMismatchException(headResult.getMessage());
            }
        }
    }

    private void processBatch(List<DataWrapper<T>> dataWrappers) {
        List<ImportBatchResultModel> resultList = handler.batchProcess(dataWrappers, requestParameter);

        for (ImportBatchResultModel result : resultList) {
            if (result.getStatus() != 0) {
                if (result.getStatus() == -1) {
                    failCollector.put(result.getIndex(), result.getMessage());
                } else {
                    skipRows++;
                }
            }
            processComments(dataWrappers.get(0));
        }
    }

    private void processOne(DataWrapper<T> dataWrapper) {
        T entity = dataWrapper.getData();
        ImportResultModel result = handler.process(entity, requestParameter, null);

        if (result.getStatus() != 0) {
            if (result.getStatus() == -1) {
                failCollector.put(dataWrapper.getIndex(), result.getMessage());
            } else {
                skipRows++;
            }
        }
        processComments(dataWrapper);
    }

    private void processComments(DataWrapper<T> dataWrapper) {
        T entity = dataWrapper.getData();
        Field[] fields = entity.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.getName().endsWith("$")) {
                field.setAccessible(true);
                try {
                    Object markVal = field.get(entity);
                    if (markVal != null) {
                        String targetFieldName = field.getName().substring(0, field.getName().length() - 1);
                        commentCollector.add(new CommentModel(dataWrapper.getIndex(), getColumnIndex(entity.getClass(), targetFieldName), markVal.toString()));
                    }
                } catch (IllegalAccessException e) {
                    LOGGER.error("处理标识失败，字段名：{}", field.getName(), e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 字段所在列数
     *
     * @param clazz     对象类
     * @param fieldName 类字段名
     * @return 从0开始的列数
     */
    private int getColumnIndex(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);

            if (excelProperty != null) {
                if (excelProperty.index() >= 0) {
                    return excelProperty.index();
                }
                for (String excelField : excelProperty.value()) {
                    Integer index = fieldMapper.get(excelField);
                    if (index != null) {
                        return index;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取字段对应列索引失败: clazz={}, fieldName={}", clazz, fieldName, e);
        }
        LOGGER.warn("字段未找到列索引: clazz={}, fieldName={}", clazz, fieldName);
        return -1;
    }
}