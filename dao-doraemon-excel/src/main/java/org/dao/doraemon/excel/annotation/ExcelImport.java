package org.dao.doraemon.excel.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Excel 导入注解
 *
 * @author sucf
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface ExcelImport {
    String code();
    ImportConfiguration configuration() default @ImportConfiguration;
}
