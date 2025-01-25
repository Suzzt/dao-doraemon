package org.dao.doraemon.sensitive.model;

import lombok.Data;

/**
 * @author sucf
 * @since 1.0
 */
@Data
public class SensitiveEntry {
    private Long key;
    private String value;

    public SensitiveEntry(Long key, String value) {
        this.key = key;
        this.value = value;
    }
}