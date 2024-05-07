package com.rakbow.kureakurusu.toolkit.handler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import lombok.extern.java.Log;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/08 13:53
 */
@MappedTypes({List.class})//java数据类型
@MappedJdbcTypes({JdbcType.VARCHAR})//数据库数据类型
@Log
public class StrListHandler extends AbstractJsonTypeHandler<List<String>> {

    public StrListHandler(Class<?> type) {
        super(type);
    }

    @Override
    public List<String> parse(String json) {
        return JsonUtil.toJavaList(json, String.class);
    }

    @Override
    public String toJson(Object obj) {
        return JsonUtil.toJson(obj);
    }
}