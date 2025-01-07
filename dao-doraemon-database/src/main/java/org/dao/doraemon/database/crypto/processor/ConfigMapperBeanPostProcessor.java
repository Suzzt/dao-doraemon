package org.dao.doraemon.database.crypto.processor;

import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Objects;
import org.dao.doraemon.database.crypto.annotated.Crypto;
import org.dao.doraemon.database.crypto.util.MetaObjectCryptoUtil;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author wuzhenhong
 * @date 2025/1/4 16:55
 */
public class ConfigMapperBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        boolean isFactoryDereference = BeanFactoryUtils.isFactoryDereference(beanName);
        // 这里只处理由MapperFactoryBean创建的对象
        if (isFactoryDereference) {
            return bean;
        }
        String factoryBeanName = BeanFactory.FACTORY_BEAN_PREFIX + beanName;
        boolean match = this.applicationContext.isTypeMatch(factoryBeanName, MapperFactoryBean.class);
        if (!match) {
            return bean;
        }
        // 获取 MapperFactoryBean 对象
        MapperFactoryBean mapperFactoryBean = this.applicationContext.getBean(factoryBeanName, MapperFactoryBean.class);
        Class<?> mapperInterface = mapperFactoryBean.getObjectType();
        return Proxy.newProxyInstance(bean.getClass().getClassLoader(), new Class[]{mapperInterface},
            (proxy, method, args) -> {
                Parameter[] parameters = method.getParameters();
                if (Objects.nonNull(parameters)) {
                    int index = 0;
                    for (Parameter parameter : parameters) {
                        Crypto enOrDecrypt = parameter.getAnnotation(Crypto.class);
                        Object value = args[index];
                        if (Objects.nonNull(enOrDecrypt) && value instanceof String ) {
                            if (Objects.nonNull(value)) {
                                args[index] = MetaObjectCryptoUtil.encryptNess(enOrDecrypt, (String) value);
                            }

                        }
                        index++;
                    }
                }
                return method.invoke(bean, args);
            });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
