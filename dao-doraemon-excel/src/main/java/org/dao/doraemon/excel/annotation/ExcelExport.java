package org.dao.doraemon.excel.annotation;

import java.lang.annotation.*;

/**
 * Excel 导出注解
 *
 * @author sucf
 * @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelExport {
    String code();
    /**
     * 根据链接下载时的文件名
     */
    String fileName() default "ExportFile";
}