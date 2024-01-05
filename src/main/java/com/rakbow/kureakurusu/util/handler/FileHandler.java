package com.rakbow.kureakurusu.util.handler;/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2024-01-05 21:17
 * @Description:
 */

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.rakbow.kureakurusu.data.system.File;
import com.rakbow.kureakurusu.util.common.JsonUtil;
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

    @Override
    protected List<File> parse(String json) {
        return JsonUtil.toJavaList(json, File.class);
    }

    @Override
    protected String toJson(List<File> obj) {
        return JsonUtil.toJson(obj);
    }
}
