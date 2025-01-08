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
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author wuzhenhong
 * @since 1.0
 */
public class ConfigMapperBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 不处理 FactoryBean
        boolean isFactoryDereference = BeanFactoryUtils.isFactoryDereference(beanName);
        if (isFactoryDereference) {
            return bean;
        }
        if (bean instanceof FactoryBean) {
            return bean;
        }
        // 判断是否存在 beanDefinition，像那种在直接在 xml 配置属性为className是没有 beanDefinition 的
        boolean exists = this.applicationContext.containsBeanDefinition(beanName);
        if (!exists) {
            return bean;
        }
        // 判断对应的工厂bean类型是否是  MapperFactoryBean
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
                        if (Objects.nonNull(enOrDecrypt) && value instanceof String) {
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
