package org.dao.doraemon.excel.imported.resolver;

import org.dao.doraemon.excel.imported.handler.ImportHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 通用解析
 *
 * @author sucf
 * @since 1.0
 */
public class GenericTypeResolver {

    /**
     * 获取泛型类型
     *
     * @param clazz 目标类
     * @return 泛型类型
     */
    public static Class<?> getGenericType(Class<?> clazz) {
        Class<?> current = clazz;
        while (current != null) {
            // 检查当前类的泛型父类
            Type genericSuperclass = current.getGenericSuperclass();
            Class<?> genericType = checkParameterizedType(genericSuperclass);
            if (genericType != null) {
                return genericType;
            }

            // 检查当前类实现的接口
            for (Type genericInterface : current.getGenericInterfaces()) {
                genericType = checkParameterizedType(genericInterface);
                if (genericType != null) {
                    return genericType;
                }
            }

            // 继续检查父类
            current = current.getSuperclass();
        }

        throw new IllegalArgumentException("无法解析泛型类型");
    }

    private static Class<?> checkParameterizedType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class) {
                Class<?> rawClass = (Class<?>) rawType;
                if (ImportHandler.class.isAssignableFrom(rawClass)) {
                    Type[] typeArgs = parameterizedType.getActualTypeArguments();
                    if (typeArgs.length > 0) {
                        return extractClass(typeArgs[0]);
                    }
                }
            }
        }
        return null;
    }

    private static Class<?> extractClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class) {
                return (Class<?>) rawType;
            }
        }
        throw new IllegalArgumentException("无法解析类型: " + type);
    }
}