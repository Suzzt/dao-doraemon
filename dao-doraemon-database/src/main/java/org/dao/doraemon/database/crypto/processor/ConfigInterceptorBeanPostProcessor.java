package org.dao.doraemon.database.crypto.processor;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dao.doraemon.database.crypto.config.DefaultCryptStrategy;
import org.dao.doraemon.database.crypto.interceptor.FieldDecryptInterceptor;
import org.dao.doraemon.database.crypto.interceptor.FieldEncryptInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author wuzhenhong
 * @since 1.0
 */
public class ConfigInterceptorBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof SqlSessionFactory) {
            // 会进入该方法的入口
            // org.springframework.beans.factory.support.FactoryBeanRegistrySupport.getObjectFromFactoryBean
            DefaultCryptStrategy.loadDefaultCrypto();
            SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) bean;
            Configuration configuration = sqlSessionFactory.getConfiguration();
            FieldEncryptInterceptor fieldEncryptInterceptor = new FieldEncryptInterceptor();
            FieldDecryptInterceptor fieldDecryptInterceptor = new FieldDecryptInterceptor();
            configuration.addInterceptor(fieldEncryptInterceptor);
            configuration.addInterceptor(fieldDecryptInterceptor);
        }
        return bean;
    }
}
