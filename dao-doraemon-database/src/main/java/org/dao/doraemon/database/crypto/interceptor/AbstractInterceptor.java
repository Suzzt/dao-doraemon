package org.dao.doraemon.database.crypto.interceptor;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;

/**
 * @author wuzhenhong
 * @date 2024/12/30 8:28
 */
public abstract class AbstractInterceptor implements Interceptor {

    protected static final ObjectFactory OBJECT_FACTORY = new DefaultObjectFactory();
    protected static final org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    protected static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();
    protected final Map<Class<?>, Object> CACHE = new ConcurrentHashMap<>(8192);

    protected <T> T getByCache(Class<?> clazz) {
        Object value = CACHE.get(clazz);
        if (Objects.nonNull(value)) {
            return (T) value;
        }
        value = OBJECT_FACTORY.create(clazz);
        CACHE.put(clazz, value);
        return (T) value;
    }

    protected MetaObject forObject(Object object) {
        return MetaObject.forObject(object, OBJECT_FACTORY, OBJECT_WRAPPER_FACTORY,
            REFLECTOR_FACTORY);
    }

}
