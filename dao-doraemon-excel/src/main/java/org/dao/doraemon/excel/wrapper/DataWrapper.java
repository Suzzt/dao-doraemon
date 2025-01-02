package org.dao.doraemon.excel.wrapper;

import lombok.Data;

/**
 * @author sucf
 * @create_time 2025/1/1 22:21
 */
@Data
public class DataWrapper<T> {
    public DataWrapper(Integer index, T data) {
        this.index = index;
        this.data = data;
    }
    private Integer index;
    private T data;
}
