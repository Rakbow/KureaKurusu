package com.rakbow.kureakurusu.toolkit.handler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.rakbow.kureakurusu.data.Link;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import lombok.extern.java.Log;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/08 11:36
 */
@MappedTypes({List.class})//java数据类型
@MappedJdbcTypes({JdbcType.VARCHAR})//数据库数据类型
@Log
public class LinkHandler extends AbstractJsonTypeHandler<List<Link>> {

    public LinkHandler(Class<?> type) {
        super(type);
    }

    @Override
    public List<Link> parse(String json) {
        return JsonUtil.toJavaList(json, Link.class);
    }

    @Override
    public String toJson(List<Link> obj) {
        return JsonUtil.toJson(obj);
    }
}
