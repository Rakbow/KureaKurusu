package com.rakbow.kureakurusu.util.handler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.util.common.JsonUtil;
import lombok.extern.java.Log;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/05 21:03
 */
@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR})        //java数据类型
@Log
public class ImageHandler extends AbstractJsonTypeHandler<List<Image>> {

    @Override
    protected List<Image> parse(String json) {
        return JsonUtil.toJavaList(json, Image.class);
    }

    @Override
    protected String toJson(List<Image> obj) {
        return JsonUtil.toJson(obj);
    }
}
