package org.dao.doraemon.sensitive.drive.retriever;

import org.dao.doraemon.sensitive.annotation.MultipleSensitive;
import org.dao.doraemon.sensitive.annotation.SensitiveMapping;
import org.dao.doraemon.sensitive.handler.Handler;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class SensitiveFieldCollector {

    public static Set<SensitiveFieldInfo> collectSensitiveFields(Object root) {
        Map<String, Class<? extends Handler>> resultMap = new LinkedHashMap<>();
        collectSensitiveFields(root, new HashSet<>(), "", resultMap, new LinkedHashMap<>());
        return resultMap.entrySet().stream()
                .map(e -> new SensitiveFieldInfo(e.getKey(), e.getValue()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private static void collectSensitiveFields(
            Object obj,
            Set<Object> visited,
            String currentPath,
            Map<String, Class<? extends Handler>> resultMap,
            Map<String, Class<? extends Handler>> classLevelHandlers
    ) {
        if (obj == null || visited.contains(obj)) {
            return;
        }
        visited.add(obj);

        Class<?> clazz = obj.getClass();

        // 处理类上的注解（作为全局处理器）
        Map<String, Class<? extends Handler>> newClassHandlers = new LinkedHashMap<>(classLevelHandlers);
        processClassAnnotations(clazz, obj, visited, currentPath, newClassHandlers);

        // 处理所有字段
        for (Field field : getAllFields(clazz)) {
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(obj);
                if (fieldValue == null) continue;

                String fieldPath = buildPath(currentPath, field.getName());

                // 处理字段上的注解（优先级高于类注解）
                Map<String, Class<? extends Handler>> fieldHandlers = new LinkedHashMap<>(newClassHandlers);
                processFieldAnnotations(field, fieldHandlers);

                // 检查是否需要处理当前字段
                checkFieldProcessing(field, fieldValue, fieldPath, resultMap, fieldHandlers);

                // 递归处理复合类型
                if (isCompositeType(field.getType())) {
                    collectSensitiveFields(fieldValue, visited, fieldPath, resultMap, fieldHandlers);
                }
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    private static void processClassAnnotations(
            Class<?> clazz,
            Object obj,
            Set<Object> visited,
            String currentPath,
            Map<String, Class<? extends Handler>> classHandlers
    ) {
        List<SensitiveMapping> mappings = new ArrayList<>();

        // 处理类上的注解
        SensitiveMapping classMapping = clazz.getAnnotation(SensitiveMapping.class);
        if (classMapping != null) mappings.add(classMapping);

        MultipleSensitive multipleSensitive = clazz.getAnnotation(MultipleSensitive.class);
        if (multipleSensitive != null) {
            Collections.addAll(mappings, multipleSensitive.value());
        }

        // 将类级处理器添加到当前路径的映射中
        for (SensitiveMapping mapping : mappings) {
            String fullPath = buildPath(currentPath, mapping.fieldName());
            classHandlers.put(fullPath, mapping.handler());
        }
    }

    private static void processFieldAnnotations(
            Field field,
            Map<String, Class<? extends Handler>> handlers
    ) {
        List<SensitiveMapping> mappings = new ArrayList<>();

        // 处理字段上的注解
        SensitiveMapping fieldMapping = field.getAnnotation(SensitiveMapping.class);
        if (fieldMapping != null) mappings.add(fieldMapping);

        MultipleSensitive multipleSensitive = field.getAnnotation(MultipleSensitive.class);
        if (multipleSensitive != null) {
            Collections.addAll(mappings, multipleSensitive.value());
        }

        // 字段级注解优先级高于类级
        for (SensitiveMapping mapping : mappings) {
            String fullPath = buildPath("", mapping.fieldName());
            handlers.put(fullPath, mapping.handler());
        }
    }

    private static void checkFieldProcessing(
            Field field,
            Object fieldValue,
            String fieldPath,
            Map<String, Class<? extends Handler>> resultMap,
            Map<String, Class<? extends Handler>> handlers
    ) {
        // 直接检查当前字段是否需要处理
        if (field.getType() == String.class) {
            // 查找所有可能匹配的处理器路径（包括通配）
            Class<? extends Handler> handler = findBestMatchHandler(fieldPath, handlers);
            if (handler != null) {
                resultMap.put(fieldPath, handler);
            }
        }
    }

    private static Class<? extends Handler> findBestMatchHandler(
            String fieldPath,
            Map<String, Class<? extends Handler>> handlers
    ) {
        // 查找最长路径匹配（最近原则）
        return handlers.entrySet().stream()
                .filter(e -> fieldPath.endsWith(e.getKey()))
                .max(Comparator.comparingInt(e -> e.getKey().length()))
                .map(Map.Entry::getValue)
                .orElse(null);
    }

    private static String buildPath(String parent, String child) {
        return parent.isEmpty() ? child : parent + "." + child;
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private static boolean isCompositeType(Class<?> type) {
        return !type.isPrimitive()
                && type != String.class
                && !type.isEnum()
                && !type.isArray()
                && !Collection.class.isAssignableFrom(type)
                && !Map.class.isAssignableFrom(type);
    }

    public static class SensitiveFieldInfo {
        private final String fieldPath;
        private final Class<? extends Handler> handler;

        public SensitiveFieldInfo(String fieldPath, Class<? extends Handler> handler) {
            this.fieldPath = fieldPath;
            this.handler = handler;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SensitiveFieldInfo that = (SensitiveFieldInfo) o;
            return Objects.equals(fieldPath, that.fieldPath) && Objects.equals(handler, that.handler);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fieldPath, handler);
        }

        public Class<? extends Handler> getHandler() {
            return handler;
        }
    }
}