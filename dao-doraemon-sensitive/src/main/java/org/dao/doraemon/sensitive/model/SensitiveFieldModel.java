package org.dao.doraemon.sensitive.model;

import lombok.Data;
import org.dao.doraemon.sensitive.handler.Handler;

import java.util.Objects;

/**
 * @author sucf
 * @since 1.0
 */
@Data
public class SensitiveFieldModel {
    private String matchedFieldName;
    private String annotationFieldName;
    private String path;
    private Handler handler;

    public SensitiveFieldModel(String matchedFieldName, String annotationFieldName, String path, Handler handler) {
        this.matchedFieldName = matchedFieldName;
        this.annotationFieldName = annotationFieldName;
        this.path = path;
        this.handler = handler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensitiveFieldModel that = (SensitiveFieldModel) o;
        return Objects.equals(path, that.path) && 
               Objects.equals(annotationFieldName, that.annotationFieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, annotationFieldName);
    }

    @Override
    public String toString() {
        return "SensitiveModel{" +
                "matchedField='" + matchedFieldName + '\'' +
                ", annotationField='" + annotationFieldName + '\'' +
                ", path='" + path + '\'' +
                ", handler=" + handler.getClass().getSimpleName() +
                '}';
    }
}