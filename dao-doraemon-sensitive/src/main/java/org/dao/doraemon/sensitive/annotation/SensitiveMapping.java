package org.dao.doraemon.sensitive.annotation;

import org.dao.doraemon.sensitive.handler.Handler;

import java.lang.annotation.*;

/**
 * 脱敏注解
 *
 * @author sucf
 * @since 1.0
 * 作用范围: 在springmvc项目中返回值，可以用于VO返回对象的字段上, 也可以用于请求处理器的方法上返回值
 * 如果作用在复合类型的对象上, 自动向下找寻字段执行你的配置实现的脱敏处理器处理逻辑
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SensitiveMapping {
    /**
     * 需要被脱敏字段名称
     */
    String fieldName();

    /**
     * 脱敏处理器
     */
    Class<? extends Handler> handler();
}
