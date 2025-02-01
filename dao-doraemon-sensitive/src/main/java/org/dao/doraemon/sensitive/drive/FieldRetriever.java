package org.dao.doraemon.sensitive.drive;

import org.dao.doraemon.sensitive.annotation.MultipleSensitive;
import org.dao.doraemon.sensitive.annotation.SensitiveMapping;
import org.dao.doraemon.sensitive.handler.Handler;
import org.dao.doraemon.sensitive.model.SensitiveFieldModel;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author sucf
 * @since 1.0
 */
public class FieldRetriever {
    private static final Logger LOGGER = Logger.getLogger(FieldRetriever.class.getName());

    public static Set<SensitiveFieldModel> parseSensitiveFields(Object obj) {
        Set<SensitiveFieldModel> sensitiveFieldModels = new HashSet<>();
        if (obj != null) {
            parseObject(obj, sensitiveFieldModels, "", null, null);
        }
        return sensitiveFieldModels;
    }

    private static void parseObject(Object obj, Set<SensitiveFieldModel> sensitiveFieldModels,
                                    String currentPath, String inheritedFieldName, Handler inheritedHandler) {
        if (obj == null) return;

        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                String fieldPath = buildPath(currentPath, field.getName());

                List<SensitiveMapping> mappings = new ArrayList<>();
                SensitiveMapping singleMapping = field.getAnnotation(SensitiveMapping.class);
                if (singleMapping != null) {
                    mappings.add(singleMapping);
                }
                MultipleSensitive multipleMapping = field.getAnnotation(MultipleSensitive.class);
                if (multipleMapping != null) {
                    mappings.addAll(Arrays.asList(multipleMapping.value()));
                }

                for (SensitiveMapping mapping : mappings) {
                    String currentAnnotationField = mapping.fieldName();
                    Handler currentHandler = mapping.handler().newInstance();

                    checkAndAddSensitiveModel(
                            field,
                            sensitiveFieldModels,
                            fieldPath,
                            currentAnnotationField,
                            currentHandler
                    );

                    Object fieldValue = field.get(obj);
                    if (fieldValue != null && !isBasicType(field.getType())) {
                        parseObject(
                                fieldValue,
                                sensitiveFieldModels,
                                fieldPath,
                                currentAnnotationField,
                                currentHandler
                        );
                    }
                }

                if (mappings.isEmpty()) {
                    checkAndAddSensitiveModel(
                            field,
                            sensitiveFieldModels,
                            fieldPath,
                            inheritedFieldName,
                            inheritedHandler
                    );

                    Object fieldValue = field.get(obj);
                    if (fieldValue != null && !isBasicType(field.getType())) {
                        parseObject(
                                fieldValue,
                                sensitiveFieldModels,
                                fieldPath,
                                inheritedFieldName,
                                inheritedHandler
                        );
                    }
                }
            } catch (Exception e) {
                LOGGER.warning("Field processing failed: " + e.getMessage());
            }
        }
    }

    private static void checkAndAddSensitiveModel(Field field, Set<SensitiveFieldModel> models,
                                                  String path, String expectedFieldName, Handler handler) {
        if (expectedFieldName == null || handler == null) return;

        if (field.getName().equals(expectedFieldName) && field.getType().equals(String.class)) {
            models.add(new SensitiveFieldModel(
                    field.getName(),
                    expectedFieldName,
                    path,
                    handler
            ));
        }
    }

    private static String buildPath(String parentPath, String fieldName) {
        return parentPath.isEmpty() ? fieldName : parentPath + "." + fieldName;
    }

    private static boolean isBasicType(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.equals(String.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Short.class);
    }
}