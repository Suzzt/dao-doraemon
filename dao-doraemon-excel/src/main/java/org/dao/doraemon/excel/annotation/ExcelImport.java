package org.dao.doraemon.excel.annotation;

/**
 * Excel 导入注解
 *
 * @author sucf
 * @create_time 2024/12/28 12:22
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
