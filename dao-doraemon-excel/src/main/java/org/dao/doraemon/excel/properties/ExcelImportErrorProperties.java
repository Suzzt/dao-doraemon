package org.dao.doraemon.excel.properties;

import lombok.Data;

/**
 * Excel 处理存在错误数据, 生成错误文件定义
 *
 * @author sucf
 * @since 1.0
 */
@Data
public class ExcelImportErrorProperties {
    /**
     * 是否需要返回生成错误文件信息
     */
    private Boolean isGenerateErrorFile = false;

    /**
     * 指定生成错误列的名称
     */
    private String errorColumnName = "Error Cause";

    /**
     * 根据链接下载错误文件名
     */
    private String errorFileName = "ExcelFailedReport.xlsx";
}
