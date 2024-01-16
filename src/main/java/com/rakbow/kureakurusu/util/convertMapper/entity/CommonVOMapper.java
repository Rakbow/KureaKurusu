package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.Gender;
import com.rakbow.kureakurusu.data.emun.common.MediaFormat;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.CommonUtil;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.JsonUtil;
import lombok.SneakyThrows;
import net.minidev.json.JSONUtil;
import org.mapstruct.Named;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

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
        return JsonUtil.toJavaList(json, String.class);
    }

    @Named("getVOTime")
    default String getVOTime(Timestamp timestamp) {
        return DateHelper.timestampToString(timestamp);
    }

    @Named("getBool")
    default Boolean getBool(int status) {
        return status == 1;
    }

    @Named("getIdsStr")
    default String getIdsStr(List<Integer> ids) {
        return JsonUtil.toJson(ids);
    }

    @Named("getGender")
    default Gender getGender(int value) {
        return Gender.get(value);
    }

    @Named("getMediaFormat")
    default List<Attribute<Integer>> getMediaFormat(String formats) {
        return MediaFormat.getAttributes(formats);
    }

}
