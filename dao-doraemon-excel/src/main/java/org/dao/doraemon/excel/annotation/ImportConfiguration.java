package org.dao.doraemon.excel.annotation;

import java.lang.annotation.*;
import java.util.List;

/**
 * 配置导入属性
 *
 * @author sucf
 * @create_time 2024/12/28 23:09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ImportConfiguration {
    /**
     * 限制导入检验最大支持行数
     */
    int maxRows() default Integer.MAX_VALUE;

    /**
     * 表头所在行数(默认从第2行作为表头)
     */
    int headRow() default 2;

    /**
     * 是否校验表头
     */
    boolean isCheckHand() default false;

    /**
     * 批量处理行数(默认是一条)
     */
    int batchProcessRows() default 1;

    /**
     * 指定跳过的行
     */
    int[] skipRow() default {};

    /**
     * 错误导入配置
     */
    ErrorImportConfiguration errorImport() default @ErrorImportConfiguration;
}
