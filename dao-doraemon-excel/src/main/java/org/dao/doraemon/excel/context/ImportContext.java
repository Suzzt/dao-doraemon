package org.dao.doraemon.excel.context;

import com.alibaba.excel.context.AnalysisContext;
import lombok.Data;

/**
 * 导入上下文内容
 *
 * @author sucf
 * @since 1.0
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
