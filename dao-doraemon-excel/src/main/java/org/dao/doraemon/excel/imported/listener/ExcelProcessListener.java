package org.dao.doraemon.excel.imported.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import lombok.Getter;
import org.dao.doraemon.excel.imported.handler.AbstractDefaultImportHandler;
import org.dao.doraemon.excel.model.ImportResultModel;
import org.dao.doraemon.excel.properties.ExcelImportProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Excel处理监听
 *
 * @author sucf
 * @create_time 2024/12/29 15:31
 */
public class ExcelProcessListener<T> extends AnalysisEventListener<T> {

    private final AbstractDefaultImportHandler<T> handler;

    private final ExcelImportProperties excelImportProperties;

    private final String requestParameter;

    public ExcelProcessListener(AbstractDefaultImportHandler<T> handler, ExcelImportProperties excelImportProperties, String requestParameter) {
        this.handler = handler;
        this.excelImportProperties = excelImportProperties;
        this.requestParameter = requestParameter;
    }

    /**
     * 失败错误收集器
     */
    @Getter
    private final Map<Integer, String> failCollector = new HashMap<>();

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
    public void invoke(T t, AnalysisContext context) {
        totalRows++;
        // todo 这里可以用bitMap来优化
        int[] skipRow = excelImportProperties.getSkipRow();
        for (int i : skipRow) {
            Integer rowIndex = context.readRowHolder().getRowIndex();
            if (i == rowIndex) {
                skipRows++;
                return;
            }
        }

        ImportResultModel result = handler.process(t, requestParameter, context);
        if (result.getStatus() != 0) {
            if (result.getStatus() == -1) {
                failCollector.put(context.readRowHolder().getRowIndex(), result.getMessage());
            } else {
                skipRows++;
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        ImportResultModel headResult = handler.checkHead(headMap, requestParameter);
        if (headResult.getStatus() == -1) {
            throw new RuntimeException(headResult.getMessage());
        }
    }
}
