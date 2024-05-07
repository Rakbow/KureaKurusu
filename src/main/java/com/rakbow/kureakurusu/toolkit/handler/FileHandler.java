package com.rakbow.kureakurusu.toolkit.handler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.rakbow.kureakurusu.data.common.File;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import lombok.extern.java.Log;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/05 21:17
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})        //java数据类型
@Log
public class FileHandler extends AbstractJsonTypeHandler<List<File>> {

    public FileHandler(Class<?> type) {
        super(type);
    }

    @Override
    public List<File> parse(String json) {
        return JsonUtil.toJavaList(json, File.class);
    }

    @Override
    public String toJson(Object obj) {
        return JsonUtil.toJson(obj);
    }
}
