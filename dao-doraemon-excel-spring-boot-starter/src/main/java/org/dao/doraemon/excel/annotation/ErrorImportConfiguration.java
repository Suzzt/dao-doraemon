package org.dao.doraemon.excel.annotation;

import java.lang.annotation.*;

/**
 * 配置Excel导入处理的提示错误属性
 *
 * @author sucf
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ErrorImportConfiguration {
    /**
     * 是否需要返回生成错误文件信息
     */
    boolean isGenerateErrorFile() default false;

    /**
     * 指定生成错误列的名称
     */
    String errorColumnName() default "Error Cause";

    /**
     * 根据链接下载错误文件名
     */
    String errorFileName() default "ExcelFailedReport";
}
