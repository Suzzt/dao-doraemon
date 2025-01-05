package org.dao.doraemon.excel.imported.listener;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;
import org.dao.doraemon.excel.exception.ExcelHeaderMismatchException;
import org.dao.doraemon.excel.exception.ExcelMarkException;
import org.dao.doraemon.excel.imported.handler.ImportHandler;
import org.dao.doraemon.excel.model.CommentModel;
import org.dao.doraemon.excel.model.ImportResultModel;
import org.dao.doraemon.excel.properties.ExcelImportProperties;
import org.dao.doraemon.excel.wrapper.DataWrapper;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Excel处理监听
 *
 * @author sucf
 * @since 2024/12/29 15:31
 */
public class ExcelProcessListener<T> extends AnalysisEventListener<T> {

    private final ImportHandler<T> handler;

    private final ExcelImportProperties excelImportProperties;

    private final String requestParameter;

    public ExcelProcessListener(ImportHandler<T> handler, ExcelImportProperties excelImportProperties, String requestParameter) {
        this.handler = handler;
        this.excelImportProperties = excelImportProperties;
        this.requestParameter = requestParameter;
    }

    /**
     * 数据收集器
     */
    private final List<DataWrapper<T>> dataCollector = new ArrayList<>();

    /**
     * 失败错误收集器
     */
    @Getter
    private final Map<Integer, String> failCollector = new HashMap<>();

    /**
     * 标识评论收集器
     */
    @Getter
    private final List<CommentModel> commentCollector = new ArrayList<>();

    /**
     * 总行数
     */
    @Getter
    private int totalRows = 0;

    /**
     * 跳过行数
     */
    @Getter
    private int skipRows = 0;

    /**
     * entity与excel表头的映射
     */
    private Map<String, Integer> fieldMapper;


    @Override
    public void invoke(T data, AnalysisContext context) {
        Integer rowIndex = context.readRowHolder().getRowIndex();
        totalRows++;
        // todo 这里可以用bitMap来优化
        int[] skipRow = excelImportProperties.getSkipRow();
        for (int i : skipRow) {
            if (i == rowIndex) {
                skipRows++;
                return;
            }
        }
        dataCollector.add(new DataWrapper<T>(rowIndex, data));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (excelImportProperties.getMaxRows() < totalRows) {
            throw new ExcelMarkException("The current quantity in the file exceeds the set maximum value (" + excelImportProperties.getMaxRows() + ").");
        }

        for (DataWrapper<T> dataWrapper : dataCollector) {
            T entity = dataWrapper.getData();
            // 处理标识
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().endsWith("$")) {
                    field.setAccessible(true);
                    Object markVal;
                    try {
                        markVal = field.get(entity);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    String targetFieldName = field.getName().substring(0, field.getName().length() - 1);
                    if (markVal != null) {
                        commentCollector.add(new CommentModel(dataWrapper.getIndex(), getColumnIndex(entity.getClass(), targetFieldName), markVal.toString()));
                    }
                }
            }
            // 处理结果
            ImportResultModel result = handler.process(entity, requestParameter, null);
            if (result.getStatus() != 0) {
                if (result.getStatus() == -1) {
                    failCollector.put(dataWrapper.getIndex(), result.getMessage());
                } else {
                    skipRows++;
                }
            }
        }
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        if (fieldMapper == null) {
            fieldMapper = new HashMap<>();
            for (Map.Entry<Integer, String> entry : headMap.entrySet()) {
                fieldMapper.put(entry.getValue(), entry.getKey());
            }
        }
        Integer rowIndex = context.readRowHolder().getRowIndex();
        if (excelImportProperties.getIsCheckHand() && Objects.equals(rowIndex, excelImportProperties.getHeadRow())) {
            ImportResultModel headResult = handler.checkHead(headMap, requestParameter);
            if (headResult.getStatus() == -1) {
                throw new ExcelHeaderMismatchException(headResult.getMessage(), "期望值", "实际值");
            }
        }
    }

    /**
     * 获取字段对应的列索引
     *
     * @param clazz     类对象
     * @param fieldName 字段名
     * @return 返回列索引
     */
    private int getColumnIndex(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty != null) {
                // 如果字段名匹配，返回对应的列索引
                if (excelProperty.index() >= 0) {
                    return excelProperty.index();
                }
                // 如果字段名不匹配，遍历@ExcelProperty value去匹配
                String[] excelFields = excelProperty.value();
                for (String excelField : excelFields) {
                    Integer index = fieldMapper.get(excelField);
                    if (index != null && index >= 0) {
                        return index;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}