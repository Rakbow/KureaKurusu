package com.rakbow.kureakurusu.util.convertMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Rakbow
 * @since 2023-01-12 10:45 Game VO转换接口
 */
@Mapper(componentModel = "spring")
public interface GameVOMapper extends MetaVOMapper {

    GameVOMapper INSTANCES = Mappers.getMapper(GameVOMapper.class);

}
