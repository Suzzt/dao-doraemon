package org.dao.doraemon.excel.context;

import com.alibaba.excel.context.AnalysisContext;
import lombok.Data;

/**
 * 导入上下文内容
 *
 * @author sucf
 * @create_time 2024/12/28 16:57
 */
@Data
public class ImportContext {
    /**
     * 当前处理行数
     */
    private Integer index;

    /**
     * 总行数
     */
    private Integer total;

    /**
     * From Easy Excel
     */
    private AnalysisContext analysisContext;
}
