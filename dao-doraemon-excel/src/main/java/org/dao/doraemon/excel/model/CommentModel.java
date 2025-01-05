package org.dao.doraemon.excel.model;

import lombok.Data;

/**
 * 标识信息Model
 *
 * @author sucf
 * @create_time 2025/1/1 18:47
 */
@Data
public class CommentModel {
    private Integer row;
    private Integer column;
    private String message;

    public CommentModel(Integer row, Integer column, String message) {
        this.row = row;
        this.column = column;
        this.message = message;
    }
}