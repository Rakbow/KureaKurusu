package com.rakbow.kureakurusu.util.convertMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Rakbow
 * @since 2023-01-12 10:45 Disc VO转换接口
 */
@Mapper(componentModel = "spring")
public interface DiscVOMapper extends MetaVOMapper {

    DiscVOMapper INSTANCES = Mappers.getMapper(DiscVOMapper.class);

}
