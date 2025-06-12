package com.rakbow.kureakurusu.toolkit.convert;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.emun.Gender;
import com.rakbow.kureakurusu.data.emun.Language;
import com.rakbow.kureakurusu.data.emun.Region;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.EnumHelper;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

    @Named("getDuration")
    default String getDuration(int duration) {
        return DateHelper.getDuration(duration);
    }

    @Named("size")
    default String size(long bytes) {
        double kb = bytes / 1024.0;
        if (kb < 100) {
            return String.format("%.2f KB", kb);
        } else {
            double mb = kb / 1024.0;
            return String.format("%.2f MB", mb);
        }
    }

    @Named("getFileExt")
    default String getFileExt(String name) {
        return name.substring(name.lastIndexOf("."));
    }

}
