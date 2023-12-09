package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.alibaba.fastjson2.JSON;
import com.rakbow.kureakurusu.data.emun.entity.album.PublishFormat;
import com.rakbow.kureakurusu.data.emun.temp.EnumUtil;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DateHelper;
import org.mapstruct.Named;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-11-05 4:03
 * @Description:
 */
public interface CommonVOMapper {

    @Named("getEnumLabel")
    default String getEnumLabel(String labelKey) {
        return I18nHelper.getMessage(labelKey);
    }

    @Named("getStrList")
    default List<String> getStrList(String json) {
        return JSON.parseArray(json).toJavaList(String.class);
    }

    @Named("getVOTime")
    default String getVOTime(Timestamp timestamp) {
        return DateHelper.timestampToString(timestamp);
    }

    @Named("getStatus")
    default Boolean getStatus(int status) {
        return status == 1;
    }

}
