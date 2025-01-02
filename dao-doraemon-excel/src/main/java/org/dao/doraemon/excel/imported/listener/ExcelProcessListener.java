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
 * @create_time 2024/12/29 15:31
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
            ImportResultModel result = handler.process(dataWrapper.getData(), requestParameter, null);
            // todo 这里要获取到用户设置的标注信息, 用commentCollector来收集
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
        Integer rowIndex = context.readRowHolder().getRowIndex();
        if (excelImportProperties.getIsCheckHand() && Objects.equals(rowIndex, excelImportProperties.getHeadRow())) {
            ImportResultModel headResult = handler.checkHead(headMap, requestParameter);
            if (headResult.getStatus() == -1) {
                throw new ExcelHeaderMismatchException(headResult.getMessage(), "期望值", "实际值");
            }
        }
    }


//    // 通过字段名和 AnalysisContext 获取对应的列索引
//    private int getColumnIndex(AnalysisContext context, String fieldName) {
//        try {
//            // 获取当前读取的表格标题行中的列名与列索引的映射关系
//            Map<Integer, String> columnIndexMap = context.readSheetHolder().getColumnIndexMap();
//
//            // 遍历映射关系，找到对应的字段名称
//            for (Map.Entry<Integer, String> entry : columnIndexMap.entrySet()) {
//                Integer columnIndex = entry.getKey();
//                String columnName = entry.getValue();
//
//                // 如果字段名称与注解中的列名一致，则返回对应的列索引
//                if (fieldName.equalsIgnoreCase(getFieldName(UserEntity.class, fieldName))) {
//                    return columnIndex;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return -1;  // 如果没有找到字段，返回 -1
//    }
//
//    // 获取字段的名称
//    private String getFieldName(Class<?> clazz, String fieldName) {
//        try {
//            Field field = clazz.getDeclaredField(fieldName);
//            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
//            if (excelProperty != null) {
//                return excelProperty.value()[0];  // 返回列名（如"姓名"）
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private int getColumnIndex(Class<?> clazz, String fieldName) {
//        try {
//            // 获取所有字段
//            Field[] fields = clazz.getDeclaredFields();
//            for (Field field : fields) {
//                // 获取字段上的 @ExcelProperty 注解
//                ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
//                if (excelProperty != null) {
//                    // 如果字段名匹配，返回对应的列索引
//                    if (field.getName().equals(fieldName)) {
//                        return excelProperty.index();
//                    }
//                }
//            }
//        } catch (Exception e) {
//        }
//        return -1;  // 如果没有找到字段，返回 -1
//    }
}