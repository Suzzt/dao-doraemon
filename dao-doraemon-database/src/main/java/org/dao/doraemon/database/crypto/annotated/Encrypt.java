package org.dao.doraemon.database.crypto.annotated;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.dao.doraemon.database.crypto.server.EncryptService;

/**
 * 标记某字段是否需要加密字段
 *
 * @author wuzhenhong
 * @date 2024/12/30 8:28
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encrypt {

    Class<EncryptService> encrypt();
}
