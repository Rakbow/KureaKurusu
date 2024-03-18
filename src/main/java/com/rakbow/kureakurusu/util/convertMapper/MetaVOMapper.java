package com.rakbow.kureakurusu.util.convertMapper;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.Gender;
import com.rakbow.kureakurusu.data.Link;
import com.rakbow.kureakurusu.data.LinkType;
import com.rakbow.kureakurusu.data.emun.common.Currency;
import com.rakbow.kureakurusu.data.emun.common.Language;
import com.rakbow.kureakurusu.data.emun.common.Region;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.vo.LinkVO;
import com.rakbow.kureakurusu.util.EnumHelper;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.JsonUtil;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import org.mapstruct.Named;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.rakbow.kureakurusu.data.common.Constant.AT;

/**
 * @author Rakbow
 * @since 2023-11-05 4:03
 */
public interface MetaVOMapper {

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

    @Named("getCurrency")
    default Currency getCurrency(String value) {
        return Currency.get(value);
    }

    @Named("getMediaFormat")
    default List<Attribute<Integer>> getMediaFormat(List<Integer> ids) {
        return EnumHelper.getAttributes(Objects.requireNonNull(MetaData.getOptions()).mediaFormatSet, ids);
    }

    @Named("getThumbCover")
    default String getThumbCover(List<Image> images) {
        return CommonImageUtil.getThumbCoverUrl(images);
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

}
