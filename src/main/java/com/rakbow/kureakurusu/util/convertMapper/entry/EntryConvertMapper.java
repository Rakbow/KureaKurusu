package com.rakbow.kureakurusu.util.convertMapper.entry;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.emun.common.Region;
import com.rakbow.kureakurusu.data.emun.entry.EntryCategory;
import com.rakbow.kureakurusu.data.emun.temp.EnumUtil;
import com.rakbow.kureakurusu.data.vo.RegionVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryVOAlpha;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.common.Company;
import com.rakbow.kureakurusu.data.entity.common.Merchandise;
import com.rakbow.kureakurusu.data.entity.common.TmpPersonnel;
import com.rakbow.kureakurusu.data.entity.common.Role;
import com.rakbow.kureakurusu.util.common.DateHelper;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-05-02 1:12
 */
@Mapper(componentModel = "spring")
public interface EntryConvertMapper {

    EntryConvertMapper INSTANCES = Mappers.getMapper(EntryConvertMapper.class);

    @Mapping(target = "category", source = "category", qualifiedByName = "getCategory")
    @Mapping(target = "alias", source = "alias", qualifiedByName = "getAlias")
    @Mapping(target = "addedTime", source = "addedTime", qualifiedByName = "getVOTime")
    @Mapping(target = "editedTime", source = "editedTime", qualifiedByName = "getVOTime")
    @Mapping(target = "detail", source = "detail", qualifiedByName = "getDetail")
    @Named("toEntryVOAlpha")
    EntryVOAlpha toEntryVOAlpha(Entry entry);

    @Mapping(target = "category", source = "category", qualifiedByName = "getCategory")
    @Mapping(target = "links", source = "detail", qualifiedByName = "getLinks")
    @Mapping(target = "alias", source = "alias", qualifiedByName = "getAlias")
    @Mapping(target = "addedTime", source = "addedTime", qualifiedByName = "getVOTime")
    @Mapping(target = "editedTime", source = "editedTime", qualifiedByName = "getVOTime")
    @Mapping(target = "region", source = "detail", qualifiedByName = "getRegion")
    @Named("toCompany")
    Company toCompany(Entry entry);

    @Mapping(target = "category", source = "category", qualifiedByName = "getCategory")
    @Mapping(target = "links", source = "detail", qualifiedByName = "getLinks")
    @Mapping(target = "alias", source = "alias", qualifiedByName = "getAlias")
    @Mapping(target = "addedTime", source = "addedTime", qualifiedByName = "getVOTime")
    @Mapping(target = "editedTime", source = "editedTime", qualifiedByName = "getVOTime")
    @Named("toPersonnel")
    TmpPersonnel toPersonnel(Entry entry);

    @Mapping(target = "category", source = "category", qualifiedByName = "getCategory")
    @Mapping(target = "alias", source = "alias", qualifiedByName = "getAlias")
    @Mapping(target = "addedTime", source = "addedTime", qualifiedByName = "getVOTime")
    @Mapping(target = "editedTime", source = "editedTime", qualifiedByName = "getVOTime")
    @Named("toRole")
    Role toRole(Entry entry);

    @Mapping(target = "category", source = "category", qualifiedByName = "getCategory")
    @Mapping(target = "alias", source = "alias", qualifiedByName = "getAlias")
    @Mapping(target = "addedTime", source = "addedTime", qualifiedByName = "getVOTime")
    @Mapping(target = "editedTime", source = "editedTime", qualifiedByName = "getVOTime")
    @Named("toMerchandise")
    Merchandise toMerchandise(Entry entry);

    @IterableMapping(qualifiedByName = "toEntryVOAlpha")
    List<EntryVOAlpha> toEntryVOAlpha(List<Entry> entries);

    @IterableMapping(qualifiedByName = "toCompany")
    List<Company> toCompany(List<Entry> entries);

    @IterableMapping(qualifiedByName = "toPersonnel")
    List<TmpPersonnel> toPersonnel(List<Entry> entries);

    @IterableMapping(qualifiedByName = "toRole")
    List<Role> toRole(List<Entry> entries);

    @IterableMapping(qualifiedByName = "toMerchandise")
    List<Merchandise> toMerchandise(List<Entry> entries);

    //region get property method

    @Named("getDetail")
    default JSONObject getDetail(String json) {
        return JSONObject.parseObject(json);
    }

    @Named("getCategory")
    default Attribute<Integer> getVOCategory(int category) {
        return EnumUtil.getAttribute(EntryCategory.class, category);
    }

    @Named("getAlias")
    default List<String> getAlias(String json) {
        return JSON.parseArray(json).toJavaList(String.class);
    }

    @Named("getLinks")
    default List<String> getLinks(String json) {
        JSONObject detail = JSON.parseObject("detail");
        return detail.getList("links", String.class);
    }

    @Named("getVOTime")
    default String getVOTime(Timestamp timestamp) {
        return DateHelper.timestampToString(timestamp);
    }

    @Named("getRegion")
    default RegionVO getVORegion(String detail) {
        String code = StringUtils.isBlank(detail)
                ? Region.GLOBAL.getCode()
                : JSON.parseObject(detail).getString("region");
        if(StringUtils.isBlank(code)) {
            code = Region.GLOBAL.getCode();
        }
        return Region.getRegion(code);
    }

    //endregion
}
