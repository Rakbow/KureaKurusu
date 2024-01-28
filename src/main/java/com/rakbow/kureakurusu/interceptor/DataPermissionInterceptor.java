package com.rakbow.kureakurusu.interceptor;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;

/**
 * @author Rakbow
 * @since 2023-11-12 5:01 权限拦截器
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataPermissionInterceptor implements InnerInterceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();

        // 获取原始 SQL
        String originalSql = boundSql.getSql();

        // TODO: 在这里添加根据权限的逻辑
        // 如果有权限，不做任何处理
        // 如果没有权限，添加额外的条件
        boolean hasPermission = checkPermission();
        if (!hasPermission) {
            // 使用反射获取私有字段 sql，并修改其值
            Field sqlField = BoundSql.class.getDeclaredField("sql");
            sqlField.setAccessible(true);
            sqlField.set(boundSql, addPermissionCondition(originalSql));
        }

        // 执行原始的 prepare 操作
        return invocation.proceed();
    }

    private boolean checkPermission() {
        // TODO: 在这里添加实际的权限判断逻辑
        // 返回 true 表示有权限，返回 false 表示无权限
        return true;
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以在这里配置一些属性
    }

    private String addPermissionCondition(String originalSql) {
        // 在原始 SQL 中添加额外的条件
        return hasWhereClause(originalSql) ? originalSql + " AND status = 1" : originalSql + " WHERE status = 1";
    }

    private boolean hasWhereClause(String sql) {
        // 简单判断 SQL 中是否已经有 WHERE 子句
        return sql.toLowerCase().contains("where");
    }
}
