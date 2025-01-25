package org.dao.doraemon.sensitive.advice;

import org.dao.doraemon.sensitive.annotation.SensitiveMapping;
import org.dao.doraemon.sensitive.model.SensitiveModel;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * FieldRetriever
 *
 * @author sucf
 * @since 1.0
 */
public class FieldRetriever {
    private final static Logger LOGGER = Logger.getLogger(FieldRetriever.class.getName());

    /**
     * 解析对象的字段，返回包含字段名和注解信息的 Set
     */
    public static Set<SensitiveModel> parseSensitiveFields(Object obj) {
        Set<SensitiveModel> sensitiveModels = new HashSet<>();
        if (obj != null) {
            parseObject(obj, sensitiveModels);
        }
        return sensitiveModels;
    }

    /**
     * 递归解析对象的字段
     */
    private static void parseObject(Object obj, Set<SensitiveModel> sensitiveModels) {
        if (obj == null) {
            return;
        }

        Class<?> clazz = obj.getClass();
        // 遍历该类的所有字段
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                // 检查字段是否有 @SensitiveMapping 注解
                if (field.isAnnotationPresent(SensitiveMapping.class)) {
                    SensitiveMapping sensitiveMapping = field.getAnnotation(SensitiveMapping.class);
                    // 记录该字段的注解配置信息
                    sensitiveModels.add(new SensitiveModel(sensitiveMapping.fieldName(), sensitiveMapping.handler().newInstance()));
                }

                Object value = field.get(obj);
                // 如果字段是 String 类型，判断它是否与注解的 fieldName 匹配
                if (field.getType().equals(String.class)) {
                    // 对于 String 类型字段，判断它是否与注解的 fieldName 匹配
                    SensitiveMapping sensitiveMapping = field.getAnnotation(SensitiveMapping.class);
                    if (sensitiveMapping != null && field.getName().equals(sensitiveMapping.fieldName()) && value != null) {
                        sensitiveModels.add(new SensitiveModel(sensitiveMapping.fieldName(), sensitiveMapping.handler().newInstance()));
                    }
                } else if (value == null || isBasicType(field.getType())) {
                    // 如果字段是基本类型，跳出递归
                    continue;
                } else {
                    // 如果是复合对象（非 String 类型、非基本类型），递归处理子对象
                    parseObject(value, sensitiveModels);
                }
            } catch (IllegalAccessException | InstantiationException e) {
                LOGGER.warning("解析脱敏字段失败：" + e.getMessage());
            }
        }
    }

    /**
     * 判断是否为基本类型
     */
    private static boolean isBasicType(Class<?> clazz) {
        return clazz.isPrimitive() || clazz.equals(String.class) || clazz.equals(Integer.class)
                || clazz.equals(Long.class) || clazz.equals(Boolean.class) || clazz.equals(Double.class)
                || clazz.equals(Float.class) || clazz.equals(Character.class) || clazz.equals(Byte.class)
                || clazz.equals(Short.class);
    }
}