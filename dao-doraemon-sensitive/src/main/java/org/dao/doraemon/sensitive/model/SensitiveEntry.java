package org.dao.doraemon.sensitive.model;

import lombok.Data;

/**
 * @author sucf
 * @since 1.0
 */
@Data
public class SensitiveEntry {
    private String key;
    private String value;

    public SensitiveEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }
}