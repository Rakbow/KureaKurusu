package com.rakbow.kureakurusu.util.convertMapper;

import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.vo.ImageVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/07 0:30
 */
@Mapper(componentModel = "spring")
public interface GeneralVOMapper {

    GeneralVOMapper INSTANCES = Mappers.getMapper(GeneralVOMapper.class);

    @Named("toVO")
    ImageVO toVO(Image image);

    @IterableMapping(qualifiedByName = "toVO")
    List<ImageVO> toVO(List<Image> image);

}
