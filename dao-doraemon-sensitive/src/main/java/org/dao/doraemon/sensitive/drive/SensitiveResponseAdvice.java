package org.dao.doraemon.sensitive.drive;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.dao.doraemon.core.utils.GsonUtils;
import org.dao.doraemon.sensitive.annotation.MultipleSensitive;
import org.dao.doraemon.sensitive.annotation.SensitiveMapping;
import org.dao.doraemon.sensitive.drive.retriever.ClassRetriever;
import org.dao.doraemon.sensitive.drive.retriever.FieldRetriever;
import org.dao.doraemon.sensitive.model.SensitiveClassModel;
import org.dao.doraemon.sensitive.model.SensitiveFieldModel;
import org.dao.doraemon.sensitive.model.SensitiveMethodModel;
import org.dao.doraemon.sensitive.serializer.SensitiveSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @author sucf
 * @since 1.0
 */
@ControllerAdvice
public class SensitiveResponseAdvice implements ResponseBodyAdvice<Object>, BeanPostProcessor {

    private final static Logger LOGGER = LoggerFactory.getLogger(SensitiveResponseAdvice.class);

    private final Map<Method, Set<SensitiveMethodModel>> sensitiveMethodMap = Maps.newHashMap();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            if (method.getAnnotation(RequestMapping.class) != null || method.getAnnotation(GetMapping.class) != null || method.getAnnotation(PostMapping.class) != null) {
                SensitiveMapping sensitiveMapping = method.getAnnotation(SensitiveMapping.class);
                if (sensitiveMapping != null) {
                    try {
                        Set<SensitiveMethodModel> sensitiveMethodModels = sensitiveMethodMap.computeIfAbsent(method, k -> Sets.newHashSet());
                        sensitiveMethodModels.add(new SensitiveMethodModel(sensitiveMapping.fieldName(), sensitiveMapping.handler().newInstance()));
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                MultipleSensitive multipleSensitive = method.getAnnotation(MultipleSensitive.class);
                if (multipleSensitive != null) {
                    try {
                        Set<SensitiveMethodModel> sensitiveMethodModels = sensitiveMethodMap.computeIfAbsent(method, k -> Sets.newHashSet());
                        for (SensitiveMapping mapping : multipleSensitive.value()) {
                            sensitiveMethodModels.add(new SensitiveMethodModel(mapping.fieldName(), mapping.handler().newInstance()));
                        }
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        Set<SensitiveMethodModel> sensitiveMethodModels = sensitiveMethodMap.get(returnType.getMethod()) == null ? Sets.newHashSet() : sensitiveMethodMap.get(returnType.getMethod());
        Set<SensitiveFieldModel> sensitiveFieldModels = FieldRetriever.parse(body);
        Set<SensitiveClassModel> sensitiveClassModels = ClassRetriever.parse(body);
        if (CollUtil.isNotEmpty(sensitiveMethodModels) || CollUtil.isNotEmpty(sensitiveFieldModels) || CollUtil.isNotEmpty(sensitiveClassModels)) {
            LOGGER.info("返回值触发脱敏！脱敏前对象={}, 脱敏字段处理信息. method={}, field={}, class={}", GsonUtils.toJson(body), sensitiveMethodModels, sensitiveFieldModels, sensitiveClassModels);
            SensitiveSerializer.setSensitiveConfig(sensitiveMethodModels, sensitiveFieldModels, sensitiveClassModels);
        }
        return body;
    }
}
