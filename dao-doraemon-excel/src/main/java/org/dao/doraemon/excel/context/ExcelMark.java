package org.dao.doraemon.excel.context;


import java.util.Map;

/**
 * @author sucf
 * @since 1.0
 */
public class ExcelMark<T> {
    private final static ThreadLocal<Map<Integer, Map<Integer, String>>> MAP_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 增加标注值
     *
     * @param field   字段名
     * @param message 标注信息
     */
    public static void add(String field, String message) {

    }
}
