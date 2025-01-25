package org.dao.doraemon.sensitive.model;

import lombok.Data;
import org.dao.doraemon.sensitive.handler.Handler;

import java.util.Objects;

/**
 * @author sucf
 * @since 1.0
 */
@Data
public class SensitiveModel {
    private String fieldName;
    private Handler handler ;

    public SensitiveModel(String fieldName, Handler handler) {
        this.fieldName = fieldName;
        this.handler = handler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensitiveModel that = (SensitiveModel) o;
        return Objects.equals(fieldName, that.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fieldName);
    }
}
