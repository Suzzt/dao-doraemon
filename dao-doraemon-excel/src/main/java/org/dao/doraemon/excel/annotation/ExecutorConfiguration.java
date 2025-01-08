package org.dao.doraemon.excel.annotation;

import java.lang.annotation.*;

/**
 * 执行器配置
 *
 * @author sucf
 * @since 2025/1/8 20:51
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExecutorConfiguration {

    /**
     * 是否开启并发执行
     */
    boolean isParallel() default false;

    /*-----------------------------------这个转化成线程池去执行----------------------------------*/

    /**
     * 执行数量
     */
    int coreNumber() default 10;

    /**
     * 容量
     */
    int capacity() default Integer.MAX_VALUE;
    /*-----------------------------------这个转化成线程池去执行----------------------------------*/
}
