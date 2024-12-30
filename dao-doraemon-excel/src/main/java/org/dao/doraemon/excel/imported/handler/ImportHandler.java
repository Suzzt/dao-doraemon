package org.dao.doraemon.excel.imported.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import org.dao.doraemon.excel.model.ImportResultModel;
import org.dao.doraemon.excel.properties.ExcelImportProperties;

import java.util.Map;

/**
 * 导入Excel处理能力接口
 *
 * @author sucf
 * @create_time 2024/12/28 16:42
 */
public interface ImportHandler<T> {

    /**
     * 处理检验Excel表头是否符合
     *
     * @param headMap          表头对象
     * @param requestParameter 请求参数
     * @return 处理结果定义
     */
    ImportResultModel checkHead(Map<Integer, ReadCellData<?>> headMap, String requestParameter);

    /**
     * 处理你的业务
     *
     * @param data             业务对象
     * @param requestParameter 请求参数
     * @param context          导入上下文
     * @return 处理结果定义
     */
    ImportResultModel process(T data, String requestParameter, AnalysisContext context);

    /**
     * 定义导入失败的错误文件名
     *
     * @param excelImportProperties excel配置
     * @return 下载时的文件名
     */
    String defineFailFileName(ExcelImportProperties excelImportProperties);
}
