package com.rakbow.kureakurusu.toolkit.handler;

import com.rakbow.kureakurusu.toolkit.JsonUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Rakbow
 * @since 2026/3/15 20:14
 */
public class JsonSetTypeHandler extends BaseTypeHandler<Set<String>> {

    // 写入数据库时使用（当前场景用不到，可空实现）
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Set<String> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtil.toJson(parameter));
    }

    // 从结果集按列名读取（核心）
    @Override
    public Set<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseJson(rs.getString(columnName));
    }

    // 从结果集按列索引读取
    @Override
    public Set<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseJson(rs.getString(columnIndex));
    }

    // 从CallableStatement读取（当前场景用不到）
    @Override
    public Set<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseJson(cs.getString(columnIndex));
    }

    // 核心解析逻辑：JSON字符串转Set<String>，兼容null/空值
    private Set<String> parseJson(String json) {
        if (json == null || json.isEmpty() || "null".equals(json)) {
            return new HashSet<>(); // 空值返回空Set，避免null
        }
        // 把JSON数组字符串转成Set（去重）
        return new HashSet<>(JsonUtil.toJavaList(json, String.class));
    }
}
