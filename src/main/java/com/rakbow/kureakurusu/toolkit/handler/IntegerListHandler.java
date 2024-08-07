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
 * @since 2024/2/22 0:52
 */
@MappedTypes({List.class})//java数据类型
@MappedJdbcTypes({JdbcType.VARCHAR})//数据库数据类型
@Log
public class IntegerListHandler extends AbstractJsonTypeHandler<List<Integer>> {

    public IntegerListHandler(Class<?> type) {
        super(type);
    }

    @Override
    public List<Integer> parse(String json) {
        return JsonUtil.toJavaList(json, Integer.class);
    }

    @Override
    public String toJson(List<Integer> obj) {
        return JsonUtil.toJson(obj);
    }
}