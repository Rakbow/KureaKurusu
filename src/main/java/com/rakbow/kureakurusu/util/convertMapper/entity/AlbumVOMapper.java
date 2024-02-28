package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.dto.album.AlbumAddDTO;
import com.rakbow.kureakurusu.data.dto.album.AlbumUpdateDTO;
import com.rakbow.kureakurusu.data.entity.Album;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.vo.album.AlbumMiniVO;
import com.rakbow.kureakurusu.data.vo.album.AlbumVO;
import com.rakbow.kureakurusu.data.vo.album.AlbumVOAlpha;
import com.rakbow.kureakurusu.data.vo.person.PersonMiniVO;
import com.rakbow.kureakurusu.util.EnumHelper;
import com.rakbow.kureakurusu.util.common.DateHelper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

/**
 * album VO转换接口
 *
 * @author Rakbow
 * @since 2023-01-11 16:13
 */
@Mapper(componentModel = "spring")
public interface AlbumVOMapper extends CommonVOMapper {

    AlbumVOMapper INSTANCES = Mappers.getMapper(AlbumVOMapper.class);

    //region single convert interface

    @Mapping(source = "hasBonus", target = "hasBonus", qualifiedByName = "getBool")
    @Named("build")
    Album build(AlbumAddDTO dto);

    /**
     * Album转VO对象，用于详情页面，转换量最大的
     *
     * @param album 专辑
     * @return AlbumVO
     * @author rakbow
     */
    @Mapping(target = "publishFormat", source = "publishFormat", qualifiedByName = "getPublishFormat")
    @Mapping(target = "albumFormat", source = "albumFormat", qualifiedByName = "getAlbumFormat")
    @Mapping(target = "mediaFormat", source = "mediaFormat", qualifiedByName = "getMediaFormat")
    @Mapping(target = "trackInfo", ignore = true)
    @ToVO
    AlbumVO toVO(Album album);

    @Mapping(target = "hasBonus", source = "hasBonus", qualifiedByName = "getBool")
    @Mapping(target = "publishFormat", source = "publishFormat", qualifiedByName = "getPublishFormat")
    @Mapping(target = "albumFormat", source = "albumFormat", qualifiedByName = "getAlbumFormat")
    @Mapping(target = "mediaFormat", source = "mediaFormat", qualifiedByName = "getMediaFormat")
    @Mapping(target = "cover", ignore = true)
    @Mapping(target = "visitNum", ignore = true)
    @ToVO
    @Named("toVOAlpha")
    AlbumVOAlpha toVOAlpha(Album album);

    @IterableMapping(qualifiedByName = "toVOAlpha")
    List<AlbumVOAlpha> toVOAlpha(List<Album> products);

    @Mapping(target = "cover", source = "images", qualifiedByName = "getThumbCover")
    @Named("toMiniVO")
    AlbumMiniVO toMiniVO(Album album);

    @IterableMapping(qualifiedByName = "toMiniVO")
    List<AlbumMiniVO> toMiniVO(List<Album> albums);

    @Named("getAlbumFormat")
    default List<Attribute<Integer>> getAlbumFormat(List<Integer> ids) {
        return EnumHelper.getAttributes(Objects.requireNonNull(MetaData.getOptions()).albumFormatSet, ids);
    }

    @Named("getPublishFormat")
    default List<Attribute<Integer>> getPublishFormat(List<Integer> ids) {
        return EnumHelper.getAttributes(Objects.requireNonNull(MetaData.getOptions()).publishFormatSet, ids);
    }

    //endregion

}
