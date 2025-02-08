package org.dao.doraemon.excel.properties;


import lombok.Data;


/**
 * 执行配置
 *
 * @author sucf
 * @since 1.0
 */
@Data
public class ExcelExecutorProperties {

    /**
     * 是否开启并行执行
     */
    private Boolean isParallel = false;

    /*-----------------------------------这个转化成线程池去执行----------------------------------*/
    /**
     * 执行数量
     */
    private Integer coreNumber = 10;

    /**
     * 容量
     */
    private Integer capacity = Integer.MAX_VALUE;
    /*-----------------------------------这个转化成线程池去执行----------------------------------*/
}
