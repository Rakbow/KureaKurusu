package com.rakbow.kureakurusu.toolkit.convert;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.common.Constant;
import com.rakbow.kureakurusu.data.emun.Gender;
import com.rakbow.kureakurusu.data.emun.Language;
import com.rakbow.kureakurusu.data.emun.Region;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author Rakbow
 * @since 2023-11-05 4:03
 */
@Mapper(componentModel = "spring")
public interface MetaVOMapper {

    //region album

    @Named("getAlbumFormat")
    default List<Attribute<Integer>> getAlbumFormat(List<Integer> ids) {
        return EnumHelper.getAttributes(Objects.requireNonNull(MetaData.getOptions()).albumFormatSet, ids);
    }

    //endregion

    @Named("toAttribute")
    @SneakyThrows
    @SuppressWarnings("unchecked")
    default <X, T extends Enum<T>> Attribute<X> toAttribute(T t) {
        Method getValueMethod = t.getClass().getMethod("getValue");
        Method getLabelMethod = t.getClass().getMethod("getLabelKey");
        String labelKey = getLabelMethod.invoke(t).toString();
        String label = I18nHelper.getMessage(labelKey);
        X value = (X) getValueMethod.invoke(t);
        return new Attribute<>(label, value);
    }

    @Named("toAttributes")
    @SneakyThrows
    @SuppressWarnings("unchecked")
    default <X, T extends Enum<T>> List<Attribute<X>> toAttributes(T t) {
        List<Attribute<X>> res = new ArrayList<>();
        Method getValueMethod = t.getClass().getMethod("getValue");
        Method getLabelMethod = t.getClass().getMethod("getLabelKey");
        String labelKey = getLabelMethod.invoke(t).toString();
        String label = I18nHelper.getMessage(labelKey);
        X value = (X) getValueMethod.invoke(t);
        res.add(new Attribute<>(label, value));
        return res;
    }

    @Named("getCurrency")
    default String getCurrency(String region) {
        if(StringUtils.isBlank(region)) return "JPY";
        if(region.equals("global")) return "JPY";
        if(region.equals("un")) return "JPY";
        if(region.equals("eu")) return "EUR";
        return Currency.getInstance(Locale.of("", region)).getCurrencyCode();
    }

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
    default Boolean getBool(int value) {
        return value == 1;
    }

    @Named("getBool")
    default int getBool(boolean value) {
        return value ? 1 : 0;
    }

    @Named("getIdsStr")
    default String getIdsStr(List<Integer> ids) {
        return JsonUtil.toJson(ids);
    }

    @Named("getGender")
    default Gender getGender(int value) {
        return Gender.get(value);
    }

    @Named("getRegion")
    default Region getRegion(String value) {
        return Region.get(value);
    }

    @Named("getLanguage")
    default Language getLanguage(String value) {
        return Language.get(value);
    }

    @Named("getMediaFormat")
    default List<Attribute<Integer>> getMediaFormat(List<Integer> ids) {
        return EnumHelper.getAttributes(Objects.requireNonNull(MetaData.getOptions()).mediaFormatSet, ids);
    }

    @Named("updateEditedTime")
    default String updateEditedTime() {
        return DateHelper.nowStr();
    }

    @Named("duration")
    default String duration(int duration) {
        return DateHelper.getDuration(duration);
    }

    @Named("size")
    default String size(long bytes) {
        return CommonUtil.getFileSize(bytes);
    }

    @Named("getFileExt")
    default String getFileExt(String name) {
        return name.substring(name.lastIndexOf("."));
    }

    @Named("thumbImage")
    default String thumbImage(String url) {
        return QiniuImageUtil.getThumb(url, 70);
    }

    @Named("displayImage")
    default String displayImage(String url) {
        return QiniuImageUtil.getThumb(url, 1200, 900);
    }

}
