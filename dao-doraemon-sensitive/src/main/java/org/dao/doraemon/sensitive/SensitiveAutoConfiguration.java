package org.dao.doraemon.sensitive;

import org.dao.doraemon.sensitive.advice.SensitiveEliminateInterceptor;
import org.dao.doraemon.sensitive.advice.SensitiveResponseAdvice;
import org.dao.doraemon.sensitive.serializer.SensitiveSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author sucf
 * @since 1.0
 * <p>
 * 在你的SpringMvc工程使用时, 作用在返回值上, 对标记敏感信息进行自动配置化脱敏能力
 * 比如你的返回值是一个User对象, 这个对象是 json 序列化后: {"id": 123456789, "name": "东方不败", "phone": "18888888888", password: "123456"}
 * {@link org.dao.doraemon.sensitive.annotation.SensitiveMapping} 标记了 phone 和 password 属性.
 * 经过自动配置化脱敏后, 返回的json是: {"id": 123456789, "name": "东方不败", "phone": {key: 1213113131, value: "18888888888"}, password: {key: 1213113132, value: "123456"}
 * 详情可以查看 {@link org.dao.doraemon.example.sensitive.SensitiveController} 中的案例
 * </p>
 */
@Configuration
public class SensitiveAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SensitiveEliminateInterceptor())
                .addPathPatterns("/**");
    }

    @Bean
    public SensitiveResponseAdvice sensitiveResponseAdvice(){
        return new SensitiveResponseAdvice();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder
                        .serializerByType(String.class, new SensitiveSerializer());
    }
}
