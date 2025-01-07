package org.dao.doraemon.database.crypto.interceptor;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.dao.doraemon.database.crypto.bo.FieldEncryptSnapshot;
import org.dao.doraemon.database.crypto.constant.MybatisFieldNameCons;

/**
 * 加密保存到数据库之后，有些业务会复用保存前的对象，此时需要将原来的数据还原
 *
 * @author wuzhenhong
 * @since 1.0
 */
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {
    Statement.class})})
public class FieldEncryptAfterInterceptor extends AbstractInterceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object returnVal = invocation.proceed();
        ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
        MetaObject metaObject = super.forObject(resultSetHandler);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue(MybatisFieldNameCons.MAPPED_STATEMENT);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        // 只处理dml语句
        if (SqlCommandType.INSERT == sqlCommandType ||
            SqlCommandType.UPDATE == sqlCommandType) {
            BoundSql boundSql = (BoundSql) metaObject.getValue(MybatisFieldNameCons.BOUND_SQL);
            List<FieldEncryptSnapshot> infos = (List<FieldEncryptSnapshot>) boundSql.getAdditionalParameter(
                FieldEncryptBeforeInterceptor.class
                    .getName().replace(".", "-"));
            if (Objects.nonNull(infos) && !infos.isEmpty()) {
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