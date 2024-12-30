package org.dao.doraemon.database.crypto.interceptor;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.dao.doraemon.database.crypto.annotated.Decrypt;
import org.dao.doraemon.database.crypto.constant.MybatisFieldNameCons;
import org.dao.doraemon.database.crypto.server.DecryptService;
import org.dao.doraemon.database.crypto.util.FieldReflectorUtil;

/**
 * mybatis解密拦截器
 *
 * @author wuzhenhong
 * @date 2024/12/30 8:28
 */
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {
    Statement.class})})
public class FieldDecryptInterceptor extends AbstractInterceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object returnVal = invocation.proceed();
        ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
        MetaObject metaObject = super.forObject(resultSetHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue(MybatisFieldNameCons.MAPPED_STATEMENT);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        // 只处理dml语句
        if (SqlCommandType.SELECT == sqlCommandType) {
            this.doGetDecryptVal(returnVal, null);
        }
        return returnVal;
    }

    //只处理bean
    private void process(Object parameter) {
        // 只处理java bean，这种字段上才可能存在注解
        List<Field> fieldList = FieldReflectorUtil.reflectFields(parameter);
        this.doProcess(parameter, fieldList);

    }

    private void doProcess(Object bean, List<Field> fieldList) {

        fieldList.stream().forEach(field -> {

            field.setAccessible(true);
            try {
                field.set(bean, this.getDecryptVal(bean, field));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("方法不允许访问！");
            }
        });
    }

    private Object getDecryptVal(Object parameter, Field field) throws IllegalAccessException {
        Object fieldBean = field.get(parameter);
        if (fieldBean == null) {
            // 没有值，不需要操作
            return null;
        }
        return this.doGetDecryptVal(fieldBean, field);
    }


    private Object doGetDecryptVal(Object fieldBean, Field field) {
        if (Objects.isNull(fieldBean)) {
            return null;
        }
        // 字段类型
        Class<?> clazz = fieldBean.getClass();
        // 只对标有注解的String类型java bean字段做加密
        if (clazz.isArray()) {
            Object[] c = (Object[]) fieldBean;
            for (Object item : c) {
                this.doGetDecryptVal(item, null);
            }
        } else if (Iterable.class.isAssignableFrom(clazz)) {
            Iterable<?> c = (Iterable<?>) fieldBean;
            for (Object item : c) {
                this.doGetDecryptVal(item, null);
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map<?, ?> map = (Map<?, ?>) fieldBean;
            map.values().stream().forEach(item -> {
                this.doGetDecryptVal(item, null);
            });
        } else if (String.class.isAssignableFrom(clazz)) {
            return this.decryptNess((String) fieldBean, field);
        } else {
            this.process(fieldBean);
        }
        return fieldBean;
    }

    private Object decryptNess(String fieldBean, Field field) {
        if (Objects.isNull(fieldBean) || Objects.isNull(field)) {
            return fieldBean;
        }
        Decrypt deCryptoAnnotation = field.getAnnotation(Decrypt.class);
        if (Objects.isNull(deCryptoAnnotation)) {
            return fieldBean;
        }
        Class<DecryptService> decryptServerClass = deCryptoAnnotation.decrypt();
        DecryptService decryptService = super.getByCache(decryptServerClass);
        String decryptedValue = decryptService.decrypt(fieldBean);
        return decryptedValue;
    }
}