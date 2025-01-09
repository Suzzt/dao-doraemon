package org.dao.doraemon.excel.annotation;

import java.lang.annotation.*;

/**
 * 配置导入属性
 *
 * @author sucf
 * @since 1.0
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
     * 表头所在行数(默认从第1行作为表头, 索引从第1行开始)
     */
    int headRow() default 1;

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
     * 执行器
     */
    ExecutorConfiguration executor() default @ExecutorConfiguration;

    /**
     * 错误导入配置
     */
    ErrorImportConfiguration errorImport() default @ErrorImportConfiguration;
}
