package org.dao.doraemon.sensitive.advice;

import org.dao.doraemon.sensitive.serializer.SensitiveSerializer;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author sucf
 * @since 1.0
 * 这个拦截器是为了清空脱敏配置传递的参数
 */
public class SensitiveEliminateInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SensitiveSerializer.removeSensitiveConfig();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
