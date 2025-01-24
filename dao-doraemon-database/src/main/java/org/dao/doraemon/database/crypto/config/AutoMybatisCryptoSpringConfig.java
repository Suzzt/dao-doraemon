package org.dao.doraemon.database.crypto.config;

import org.dao.doraemon.database.crypto.aspect.CryptoAspect;
import org.dao.doraemon.database.crypto.processor.ConfigInterceptorBeanPostProcessor;
import org.dao.doraemon.database.crypto.processor.ConfigMapperBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 当加解密插件是通过jar包利用springboot的自动装配的提供方式时，不修改原项目中的 SqlSessionFactory 配置，
 * 那么可以通过以下方式动态加入
 *
 * @author wuzhenhong
 * @since 1.0
 */
@Configuration
public class AutoMybatisCryptoSpringConfig {

    @Bean
    public CryptoAspect cryptoAspect() {
        return new CryptoAspect();
    }

    @Bean
    public ConfigInterceptorBeanPostProcessor configInterceptorBeanPostProcessor() {
        return new ConfigInterceptorBeanPostProcessor();
    }

    @Bean
    public ConfigMapperBeanPostProcessor configMapperBeanPostProcessor() {
        return new ConfigMapperBeanPostProcessor();
    }
}
