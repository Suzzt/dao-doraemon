package org.dao.doraemon.database.crypto.interceptor;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.dao.doraemon.database.crypto.util.MetaObjectCryptoUtil;

/**
 * @author wuzhenhong
 * @SInCE 1.0
 */
public abstract class AbstractInterceptor implements Interceptor {

    protected <T> T getByCache(Class<?> clazz) {
        return MetaObjectCryptoUtil.getByCache(clazz);
    }

    protected MetaObject forObject(Object object) {
        return MetaObjectCryptoUtil.forObject(object);
    }

}
