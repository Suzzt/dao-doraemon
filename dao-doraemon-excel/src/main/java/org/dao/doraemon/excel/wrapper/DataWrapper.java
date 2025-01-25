package org.dao.doraemon.excel.wrapper;

import lombok.Data;

/**
 * @author sucf
 * @since 1.0
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
