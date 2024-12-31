package org.dao.doraemon.excel.properties;

import lombok.Data;

/**
 * 导入配置设置信息
 * 配置中涉及到的数字都是从1开始的, 而不是从0开始.
 *
 * @author sucf
 * @create_time 2024/12/26 20:28
 */
@Data
public class ExcelImportProperties {
    /**
     * 限制导入检验最大支持行数
     */
    private Integer maxRows;

    /**
     * 表头所在行数(默认从第1行作为表头, 索引从第1行开始)
     */
    private Integer headRow = 1;

    /**
     * 是否校验表头
     */
    private Boolean isCheckHand = false;

    /**
     * 批量处理行数(默认是一条)
     */
    private Integer batchProcessRows = 1;

    /**
     * 指定跳过的行
     */
    private int[] skipRow = {};

    /**
     * 定义错误提示文件信息
     */
    private ExcelImportErrorProperties excelImportErrorProperties;
}
