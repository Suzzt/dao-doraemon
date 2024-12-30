package org.dao.doraemon.database.crypto.spring.annotated;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.dao.doraemon.database.crypto.spring.starter.AutoMybatisInterceptorSpringConfig;
import org.springframework.context.annotation.Import;

/**
 * 非springboot项目可以直接在启动类上标注该注解
 * @author wuzhenhong
 * @date 2024/12/27 16:52
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(AutoMybatisInterceptorSpringConfig.class)
public @interface EnableCrypt {

}
