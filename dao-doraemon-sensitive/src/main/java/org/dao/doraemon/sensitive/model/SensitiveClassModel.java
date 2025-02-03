package org.dao.doraemon.sensitive.model;

import lombok.Data;
import org.dao.doraemon.sensitive.handler.Handler;

import java.util.Objects;

/**
 * @author sucf
 * @since 1.0
 */
@Data
public class SensitiveClassModel {
    private String fieldPath;
    private Handler handler;

    public SensitiveClassModel(String fieldPath, Handler handler) {
        this.fieldPath = fieldPath;
        this.handler = handler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensitiveClassModel that = (SensitiveClassModel) o;
        return Objects.equals(fieldPath, that.fieldPath) && Objects.equals(handler, that.handler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldPath, handler);
    }
}