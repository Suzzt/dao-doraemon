package org.dao.doraemon.sensitive.drive.retriever;

import org.dao.doraemon.sensitive.annotation.MultipleSensitive;
import org.dao.doraemon.sensitive.annotation.SensitiveMapping;
import org.dao.doraemon.sensitive.handler.Handler;
import org.dao.doraemon.sensitive.model.SensitiveClassModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author sucf
 * @since 1.0
 */
public class ClassRetriever {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassRetriever.class);

    public static Set<SensitiveClassModel> parse(Object root) {
        return collectRecursive(root, new HashSet<>(), "", new HashMap<>());
    }

    private static Set<SensitiveClassModel> collectRecursive(Object obj, Set<Object> visited, String parentPath, Map<String, Class<? extends Handler>> parentHandlers) {
        Set<SensitiveClassModel> results = new HashSet<>();
        if (obj == null || visited.contains(obj)) return results;
        visited.add(obj);

        // 合并处理规则：当前类注解 + 父级规则（父级规则可能被覆盖）
        Map<String, Class<? extends Handler>> currentHandlers = new HashMap<>(parentHandlers);
        currentHandlers.putAll(parseClassAnnotations(obj.getClass(), parentPath));

        for (Field field : getAllFields(obj.getClass())) {
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(obj);
                if (fieldValue == null) continue;

                String fieldName = field.getName();
                String currentPath = parentPath.isEmpty() ? fieldName : parentPath + "." + fieldName;

                // 处理字段上的注解（最高优先级）
                Map<String, Class<? extends Handler>> fieldHandlers = parseFieldAnnotations(field, currentPath);
                currentHandlers.putAll(fieldHandlers);

                // 检查是否匹配处理规则
                if (currentHandlers.containsKey(fieldName)) {
                    if (field.getType() == String.class) {
                        results.add(new SensitiveClassModel(currentPath, currentHandlers.get(fieldName).newInstance()));
                    }
                }

                // 递归处理复合类型（携带当前规则）
                if (isCompositeType(field.getType())) {
                    results.addAll(collectRecursive(fieldValue, visited, currentPath, currentHandlers));
                }
            } catch (IllegalAccessException | InstantiationException e) {
                LOGGER.error("Failed to sensitive fields", e);
            }
        }
        return results;
    }

    private static Map<String, Class<? extends Handler>> parseClassAnnotations(Class<?> clazz, String basePath) {
        Map<String, Class<? extends Handler>> handlers = new HashMap<>();

        // 处理类级别注解
        SensitiveMapping classMapping = clazz.getAnnotation(SensitiveMapping.class);
        if (classMapping != null) {
            handlers.put(classMapping.fieldName(), classMapping.handler());
        }

        MultipleSensitive multiple = clazz.getAnnotation(MultipleSensitive.class);
        if (multiple != null) {
            for (SensitiveMapping mapping : multiple.value()) {
                handlers.put(mapping.fieldName(), mapping.handler());
            }
        }

        return handlers;
    }

    private static Map<String, Class<? extends Handler>> parseFieldAnnotations(Field field, String basePath) {
        Map<String, Class<? extends Handler>> handlers = new HashMap<>();

        // 处理字段级别注解
        SensitiveMapping fieldMapping = field.getAnnotation(SensitiveMapping.class);
        if (fieldMapping != null) {
            handlers.put(fieldMapping.fieldName(), fieldMapping.handler());
        }

        MultipleSensitive multiple = field.getAnnotation(MultipleSensitive.class);
        if (multiple != null) {
            for (SensitiveMapping mapping : multiple.value()) {
                handlers.put(mapping.fieldName(), mapping.handler());
            }
        }
        return handlers;
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private static boolean isCompositeType(Class<?> type) {
        return !type.isPrimitive() && !String.class.isAssignableFrom(type) && !type.isEnum() && !type.isArray() && !Collection.class.isAssignableFrom(type) && !Map.class.isAssignableFrom(type);
    }
}