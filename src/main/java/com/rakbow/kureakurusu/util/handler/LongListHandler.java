package com.rakbow.kureakurusu.util.handler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.rakbow.kureakurusu.util.common.JsonUtil;
import lombok.extern.java.Log;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/3/3 22:59
 */
@MappedTypes({List.class})//java数据类型
@MappedJdbcTypes({JdbcType.VARCHAR})//数据库数据类型
@Log
public class LongListHandler extends AbstractJsonTypeHandler<List<Long>> {

    @Override
        protected List<Long> parse(String json) {
        return JsonUtil.toJavaList(json, Long.class);
    }

    @Override
    protected String toJson(List<Long> obj) {
        return JsonUtil.toJson(obj);
    }
}
