package org.dao.doraemon.database.crypto.aspect;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.dao.doraemon.database.crypto.annotated.Crypto;
import org.dao.doraemon.database.crypto.util.MetaObjectCryptoUtil;

/**
 * @author wuzhenhong
 * @SInCE 1.0
 */
@Aspect
public class CryptoAspect {

    @Before("@annotation(org.dao.doraemon.database.crypto.annotated.Crypto)")
    public void beforePointcut(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            Parameter[] parameters = method.getParameters();
            if (Objects.nonNull(parameters)) {
                Object[] args = joinPoint.getArgs();
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
        }
    }
}
