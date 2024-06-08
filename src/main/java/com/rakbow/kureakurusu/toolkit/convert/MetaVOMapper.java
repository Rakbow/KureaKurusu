package com.rakbow.kureakurusu.toolkit.convert;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.Link;
import com.rakbow.kureakurusu.data.emun.*;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.vo.LinkVO;
import com.rakbow.kureakurusu.toolkit.*;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import java.util.Currency;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    @Named("getPublishFormat")
    default List<Attribute<Integer>> getPublishFormat(List<Integer> ids) {
        return EnumHelper.getAttributes(Objects.requireNonNull(MetaData.getOptions()).publishFormatSet, ids);
    }

    //endregion

    //region book

    @Named("getBookType")
    default BookType getBookType(int value) {
        return BookType.get(value);
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
        if(region.equals("global")) return "JPY";
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

    @Named("getLinks")
    default List<LinkVO> getLinks(List<Link> links) {
        List<LinkVO> res = new ArrayList<>();
        if(links.isEmpty()) return res;
        for (Link link : links) {
            res.add(new LinkVO(link));
        }
        return res;
    }

    @Named("getFranchise")
    default Attribute<Long> getFranchise(long value) {
        return DataFinder.findAttributeByValue(value, Objects.requireNonNull(MetaData.getOptions()).franchiseSet);
    }

}
