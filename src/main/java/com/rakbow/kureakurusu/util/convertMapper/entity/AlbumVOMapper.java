package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.dto.album.AlbumAddDTO;
import com.rakbow.kureakurusu.data.dto.album.AlbumUpdateDTO;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.entity.album.AlbumFormat;
import com.rakbow.kureakurusu.data.emun.entity.album.PublishFormat;
import com.rakbow.kureakurusu.data.emun.temp.EnumUtil;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.vo.album.AlbumVO;
import com.rakbow.kureakurusu.data.vo.album.AlbumVOAlpha;
import com.rakbow.kureakurusu.data.vo.album.AlbumVOBeta;
import com.rakbow.kureakurusu.data.vo.album.AlbumVOGamma;
import com.rakbow.kureakurusu.data.entity.Album;
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
    Entity ENTITY = Entity.ALBUM;
    int entityTypeId = Entity.ALBUM.getValue();

    //region single convert interface

    @Mapping(source = "releaseDate", target = "releaseDate", dateFormat = DateHelper.DATE_FORMAT)
    @Mapping(source = "publishFormat", target = "publishFormat", qualifiedByName = "getIdsStr")
    @Mapping(source = "albumFormat", target = "albumFormat", qualifiedByName = "getIdsStr")
    @Mapping(source = "mediaFormat", target = "mediaFormat", qualifiedByName = "getIdsStr")
    @Named("build")
    Album build(AlbumAddDTO dto);

    @Mapping(source = "releaseDate", target = "releaseDate", dateFormat = DateHelper.DATE_FORMAT)
    @Mapping(source = "publishFormat", target = "publishFormat", qualifiedByName = "getIdsStr")
    @Mapping(source = "albumFormat", target = "albumFormat", qualifiedByName = "getIdsStr")
    @Mapping(source = "mediaFormat", target = "mediaFormat", qualifiedByName = "getIdsStr")
    @Named("build")
    Album build(AlbumUpdateDTO dto);

    /**
     * Album转VO对象，用于详情页面，转换量最大的
     *
     * @param album 专辑
     * @return AlbumVO
     * @author rakbow
     */
    @Mapping(target = "releaseDate", source = "releaseDate", dateFormat = DateHelper.DATE_FORMAT)
    @Mapping(target = "publishFormat", source = "publishFormat", qualifiedByName = "getPublishFormat")
    @Mapping(target = "albumFormat", source = "albumFormat", qualifiedByName = "getAlbumFormat")
    @Mapping(target = "mediaFormat", source = "mediaFormat", qualifiedByName = "getMediaFormat")
    @Mapping(target = "trackInfo", ignore = true)
    @ToVO
    AlbumVO toVO(Album album);

    // /**
    //  * Album转VO，供album-list和album-index界面使用，信息量较少
    //  *
    //  * @param album 专辑
    //  * @return AlbumVOAlpha
    //  * @author rakbow
    //  */
    // @Named("toVOAlpha")
    // AlbumVOAlpha toVOAlpha(Album album);
    //
    // /**
    //  * Album转VO对象，信息量最少
    //  *
    //  * @param album 专辑
    //  * @return AlbumVOBeta
    //  * @author rakbow
    //  */
    // @Named("toVOBeta")
    // AlbumVOBeta toVOBeta(Album album);
    //
    // /**
    //  * Album转VO对象，用于存储到搜索引擎
    //  *
    //  * @param album 专辑
    //  * @return AlbumVOGamma
    //  * @author rakbow
    //  */
    // @Named("toVOGamma")
    // AlbumVOGamma toVOGamma(Album album);

    //endregion

    //region multi convert interface

    // /**
    //  * 列表转换, Album转VO对象，供album-list和album-index界面使用，信息量较少
    //  *
    //  * @param albums 专辑列表
    //  * @return List<AlbumVOAlpha>
    //  * @author rakbow
    //  */
    // @IterableMapping(qualifiedByName = "toVOAlpha")
    // List<AlbumVOAlpha> toVOAlpha(List<Album> albums);
    //
    // /**
    //  * 列表转换, Album转VO对象，信息量最少
    //  *
    //  * @param albums 专辑列表
    //  * @return List<AlbumVOBeta>
    //  * @author rakbow
    //  */
    // @IterableMapping(qualifiedByName = "toVOBeta")
    // List<AlbumVOBeta> toVOBeta(List<Album> albums);
    //
    // /**
    //  * 列表转换, Album转VO对象，用于存储到搜索引擎
    //  *
    //  * @param albums 专辑列表
    //  * @return List<AlbumVOGamma>
    //  * @author rakbow
    //  */
    // @IterableMapping(qualifiedByName = "toVOGamma")
    // List<AlbumVOGamma> toVOGamma(List<Album> albums);

    //endregion

    //region get property method

    @Named("getAlbumFormat")
    default List<Attribute<Integer>> getAlbumFormat(String formats) {
        return EnumHelper.getAttributes(Objects.requireNonNull(MetaData.getOptions()).albumFormatSet, formats);
    }

    @Named("getPublishFormat")
    default List<Attribute<Integer>> getPublishFormat(String formats) {
        return EnumHelper.getAttributes(Objects.requireNonNull(MetaData.getOptions()).publishFormatSet, formats);
    }

    //endregion

}
