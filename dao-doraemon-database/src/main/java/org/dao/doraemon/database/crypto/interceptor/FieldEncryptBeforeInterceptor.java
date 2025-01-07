package org.dao.doraemon.database.crypto.interceptor;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.dao.doraemon.database.crypto.annotated.Crypto;
import org.dao.doraemon.database.crypto.bo.FieldEncryptSnapshot;
import org.dao.doraemon.database.crypto.constant.MybatisFieldNameCons;
import org.dao.doraemon.database.crypto.util.FieldReflectorUtil;
import org.dao.doraemon.database.crypto.util.MetaObjectCryptoUtil;
import org.dao.doraemon.database.crypto.util.ThreadLocalUtil;

/**
 * insert与update时对数据加密
 *
 * @author wuzhenhong
 * @SInCE 1.0
 */
@Intercepts({@Signature(type = ParameterHandler.class, method = "setParameters", args = {
    PreparedStatement.class})})
public class FieldEncryptBeforeInterceptor extends AbstractInterceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        MetaObject metaObject = super.forObject(parameterHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue(MybatisFieldNameCons.MAPPED_STATEMENT);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        // 只处理dml语句
        if (SqlCommandType.INSERT == sqlCommandType ||
            SqlCommandType.UPDATE == sqlCommandType) {
            BoundSql boundSql = (BoundSql) metaObject.getValue(MybatisFieldNameCons.BOUND_SQL);
            Object parameter = parameterHandler.getParameterObject();
            try {
                this.execEncrypt(parameter);
                List<FieldEncryptSnapshot> infos = ThreadLocalUtil.get();
                if (Objects.nonNull(infos)) {
                    boundSql.setAdditionalParameter(FieldEncryptBeforeInterceptor.class
                        .getName().replace(".", "-"), infos);
                }
            } finally {
                ThreadLocalUtil.remove();
            }
        }
        return invocation.proceed();
    }

    private void execEncrypt(Object parameter) throws IllegalAccessException {
        if (Objects.isNull(parameter)) {
            return;
        }
        if (parameter instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) parameter;
            List<Object> itemRepeatList = new ArrayList<>();
            map.values().forEach(item -> {
                for (Object repeat : itemRepeatList) {
                    if (repeat == item) {
                        return;
                    }
                }
                this.doGetEncryptVal(item, null);
                itemRepeatList.add(item);
            });
        } else {
            this.doGetEncryptVal(parameter, null);
        }
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
                field.set(bean, this.getEncryptVal(bean, field));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("方法不允许访问！");
            }
        });
    }

    private Object getEncryptVal(Object parameter, Field field) throws IllegalAccessException {
        Object fieldBean = field.get(parameter);
        if (fieldBean == null) {
            // 没有值，不需要操作
            return null;
        }
        return this.doGetEncryptVal(fieldBean, field, parameter);
    }

    private Object doGetEncryptVal(Object fieldBean, Field field) {
        return this.doGetEncryptVal(fieldBean, field, null);
    }

    private Object doGetEncryptVal(Object fieldBean, Field field, Object containBean) {
        if (Objects.isNull(fieldBean)) {
            return null;
        }
        // 字段类型
        Class<?> clazz = fieldBean.getClass();
        // 只对标有注解的String类型java bean字段做加密
        if (clazz.isArray()) {
            Object[] c = (Object[]) fieldBean;
            for (Object item : c) {
                this.doGetEncryptVal(item, null);
            }
        } else if (Iterable.class.isAssignableFrom(clazz)) {
            Iterable<?> c = (Iterable<?>) fieldBean;
            for (Object item : c) {
                this.doGetEncryptVal(item, null);
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map<?, ?> map = (Map<?, ?>) fieldBean;
            map.values().stream().forEach(item -> {
                this.doGetEncryptVal(item, null);
            });
        } else if (String.class.isAssignableFrom(clazz)) {
            return this.encryptNess((String) fieldBean, field, containBean);
        } else {
            this.process(fieldBean);
        }
        return fieldBean;
    }

    private Object encryptNess(String fieldBean, Field field, Object containBean) {
        if (Objects.isNull(fieldBean) || Objects.isNull(field)) {
            return fieldBean;
        }
        Crypto enOrDecryptAnnotation = field.getAnnotation(Crypto.class);
        if (Objects.isNull(enOrDecryptAnnotation)) {
            return fieldBean;
        }
        String encryptedValue = MetaObjectCryptoUtil.encryptNess(enOrDecryptAnnotation, fieldBean);
        List<FieldEncryptSnapshot> infos = ThreadLocalUtil.get();
        if (Objects.isNull(infos)) {
            infos = new ArrayList<>();
            ThreadLocalUtil.set(infos);
        }
        FieldEncryptSnapshot info = new FieldEncryptSnapshot();
        info.setContainBean(containBean);
        info.setOrigin(fieldBean);
        info.setEncrypt(encryptedValue);
        info.setField(field);
        infos.add(info);
        return encryptedValue;
    }
}