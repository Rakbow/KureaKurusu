package com.rakbow.kureakurusu.toolkit.convert;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.EnumHelper;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.lang.reflect.Method;
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
    default <X, E extends Enum<E>> Attribute<X> toAttribute(E e) {
        Method getValueMethod = e.getClass().getMethod("getValue");
        Method getLabelMethod = e.getClass().getMethod("getLabelKey");
        String labelKey = getLabelMethod.invoke(e).toString();
        String label = I18nHelper.getMessage(labelKey);
        X value = (X) getValueMethod.invoke(e);
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
        if(StringUtils.isBlank(region) || StringUtils.equals(region, "un")) {
            return "JPY";
        }else {
            return Currency.getInstance(Locale.of("", region)).getCurrencyCode();
        }
    }

    @Named("getMediaFormat")
    default List<Attribute<Integer>> getMediaFormat(List<Integer> ids) {
        return EnumHelper.getAttributes(Objects.requireNonNull(MetaData.getOptions()).mediaFormatSet, ids);
    }

    @Named("duration")
    default String duration(int duration) {
        return DateHelper.getDuration(duration);
    }

    @Named("size")
    default String size(long bytes) {
        return CommonUtil.getFileSize(bytes);
    }

    @Named("thumbImage")
    default String thumbImage(String url) {
        return QiniuImageUtil.getThumb(url, 70);
    }

    @Named("displayImage")
    default String displayImage(String url) {
        return QiniuImageUtil.getThumb(url, 1100, 700);
    }

}
