package org.dao.doraemon.database.crypto.annotated;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.dao.doraemon.database.crypto.server.DecryptService;
import org.dao.doraemon.database.crypto.server.EncryptService;

/**
 * 标记某字段是否需要加密字段
 *
 * @author wuzhenhong
 * @since 1.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Crypto {

    /**
     * 被标注的字段是否需要加密
     */
    boolean encrypt() default true;

    /**
     * 被标注的字段是否需要解密
     */
    boolean decrypt() default true;

    Class<? extends EncryptService>[] encryptClass() default {};

    Class<? extends DecryptService>[] decryptClass() default {};
}
