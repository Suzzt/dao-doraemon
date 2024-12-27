package org.dao.doraemon.database.crypto.annotated;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.dao.doraemon.database.crypto.server.DecryptServer;

/**
 * 解密标记
 * @author wuzhenhong
 * @date 2024/12/27 9:48
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Decrypt {

    Class<DecryptServer> decrypt();
}
