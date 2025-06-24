package com.rakbow.kureakurusu.toolkit.handler;

import com.rakbow.kureakurusu.data.emun.AlbumFormat;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2025/6/24 14:56
 */
@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR) // 或 JdbcType.OTHER 对应 JSON 类型
public class AlbumFormatTypeHandler extends BaseTypeHandler<List<AlbumFormat>> {

    @Override
    @SneakyThrows
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    List<AlbumFormat> parameter, JdbcType jdbcType) {
        List<Integer> values = parameter.stream().map(AlbumFormat::getValue).collect(Collectors.toList());
        ps.setString(i, JsonUtil.toJson(values));
    }

    @Override
    @SneakyThrows
    public List<AlbumFormat> getNullableResult(ResultSet rs, String columnName) {
        return parseJsonArray(rs.getString(columnName));
    }

    @Override
    @SneakyThrows
    public List<AlbumFormat> getNullableResult(ResultSet rs, int columnIndex) {
        return parseJsonArray(rs.getString(columnIndex));
    }

    @Override
    @SneakyThrows
    public List<AlbumFormat> getNullableResult(CallableStatement cs, int columnIndex) {
        return parseJsonArray(cs.getString(columnIndex));
    }

    @SneakyThrows
    private List<AlbumFormat> parseJsonArray(String json) {
        if (StringUtils.isBlank(json)) return Collections.emptyList();
        return JsonUtil.toJavaList(json, Integer.class).stream()
                .map(AlbumFormat::get)
                .collect(Collectors.toList());
    }
}
