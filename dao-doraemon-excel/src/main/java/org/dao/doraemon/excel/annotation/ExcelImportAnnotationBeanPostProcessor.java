package org.dao.doraemon.excel.annotation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

/**
 * Excel import scan load bean post processor
 *
 * @author sucf
 * @create_time 2024/12/28 22:51
 */
public class ExcelImportAnnotationBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor {

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (beanClass.isAnnotationPresent(ExcelImport.class)) {
            ExcelImport annotation = beanClass.getAnnotation(ExcelImport.class);
            String code = annotation.code();
            ImportConfiguration configuration = annotation.configuration();
            // todo 怎么样把这个对象Dispatcher中的放到resource中，key=code  value=这个bean对象
        }
        return SmartInstantiationAwareBeanPostProcessor.super.postProcessBeforeInstantiation(beanClass, beanName);
    }
}
