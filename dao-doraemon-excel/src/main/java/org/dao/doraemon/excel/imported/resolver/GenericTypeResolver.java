package org.dao.doraemon.excel.imported.resolver;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 通用解析
 *
 * @author sucf
 * @create_time 2024/12/29 15:45
 */
public class GenericTypeResolver {

    /**
     * 获取泛型类型
     *
     * @param clazz
     * @return
     */
    public static Class<?> getGenericType(Class<?> clazz) {
        Type superclass = clazz.getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superclass;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (typeArguments.length > 0 && typeArguments[0] instanceof Class<?>) {
                return (Class<?>) typeArguments[0];
            }
        }
        throw new IllegalArgumentException("无法解析泛型类型");
    }
}

