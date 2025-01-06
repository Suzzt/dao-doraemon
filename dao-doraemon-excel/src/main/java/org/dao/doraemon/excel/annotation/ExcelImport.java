package org.dao.doraemon.excel.annotation;

/**
 * Excel 导入注解
 *
 * @author sucf
 * @since 1.0
 */

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface ExcelImport {
    String code();
    ImportConfiguration configuration() default @ImportConfiguration;
}
