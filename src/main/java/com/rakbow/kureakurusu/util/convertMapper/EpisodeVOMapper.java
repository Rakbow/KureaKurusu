package com.rakbow.kureakurusu.util.convertMapper;

import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.vo.episode.EpisodeVOAlpha;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Episode VO转换接口
 *
 * @author Rakbow
 * @since 2024-01-29 13:53
 */
@Mapper(componentModel = "spring")
public interface EpisodeVOMapper extends MetaVOMapper {

    EpisodeVOMapper  INSTANCES = Mappers.getMapper(EpisodeVOMapper.class);

    @Named("toVOAlpha")
    EpisodeVOAlpha toVOAlpha(Episode ep);

    @IterableMapping(qualifiedByName = "toVOAlpha")
    List<EpisodeVOAlpha> toVOAlpha(List<Episode> eps);

}
