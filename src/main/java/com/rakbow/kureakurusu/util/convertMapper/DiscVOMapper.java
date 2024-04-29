package com.rakbow.kureakurusu.util.convertMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Rakbow
 * @since 2023-01-12 10:45 Disc VO转换接口
 */
@Mapper(componentModel = "spring", uses = MetaVOMapper.class)
public interface DiscVOMapper {

    DiscVOMapper INSTANCES = Mappers.getMapper(DiscVOMapper.class);

}
