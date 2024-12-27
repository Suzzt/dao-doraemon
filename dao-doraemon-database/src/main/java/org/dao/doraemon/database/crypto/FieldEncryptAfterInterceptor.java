package org.dao.doraemon.database.crypto;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;

@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {
    Statement.class})})
public class FieldEncryptAfterInterceptor implements Interceptor {

    private static final ObjectFactory OBJECT_FACTORY = new DefaultObjectFactory();
    private static final org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory REFLECTOR_FACTORY = new DefaultReflectorFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object returnVal = invocation.proceed();
        ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(resultSetHandler, OBJECT_FACTORY, OBJECT_WRAPPER_FACTORY,
            REFLECTOR_FACTORY);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        // 只处理dml语句
        if (SqlCommandType.INSERT == sqlCommandType ||
            SqlCommandType.UPDATE == sqlCommandType) {
            BoundSql boundSql = (BoundSql) metaObject.getValue("boundSql");
            List<FieldEncryptSnapshotInfo> infos = (List<FieldEncryptSnapshotInfo>) boundSql.getAdditionalParameter(
                FieldEncryptBeforeInterceptor.class
                    .getName().replace(".", "-"));
            if(Objects.nonNull(infos) && !infos.isEmpty()) {
                infos.stream().forEach(info -> {
                    Field field = info.getField();
                    field.setAccessible(true);
                    try {
                        // 还原调用插入和更新的值，以便业务复用这些对象
                        field.set(info.getContainBean(), info.getOrigin());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        return returnVal;
    }
}