package org.dao.doraemon.sensitive.annotation;

import java.lang.annotation.*;

/**
 * 脱敏注解
 * 多个字段同时配置, 就是@SensitiveMapping数组的意义.
 * {@link org.dao.doraemon.sensitive.annotation.SensitiveMapping}
 *
 * @author sucf
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultipleSensitive {
    SensitiveMapping[] value();
}
