package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.alibaba.fastjson2.JSON;
import com.rakbow.kureakurusu.data.Gender;
import com.rakbow.kureakurusu.data.emun.entity.album.PublishFormat;
import com.rakbow.kureakurusu.data.emun.temp.EnumUtil;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.CommonUtil;
import com.rakbow.kureakurusu.util.common.DateHelper;
import org.mapstruct.Named;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2023-11-05 4:03
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

    @Named("getDate")
    default Date getDate(String date) throws ParseException { return DateHelper.stringToDate(date); }

    @Named("getIdsStr")
    default String getIdsStr(List<Integer> ids) {
        return CommonUtil.getIdsStr(ids);
    }

    @Named("getGender")
    default Gender getGender(int value) {
        return Gender.get(value);
    }

}
