package com.rakbow.kureakurusu.util.convertMapper;

import com.rakbow.kureakurusu.annotation.ToVO;
import com.rakbow.kureakurusu.data.entity.Book;
import com.rakbow.kureakurusu.data.vo.book.BookMiniVO;
import com.rakbow.kureakurusu.data.vo.book.BookVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * book VO转换接口
 *
 * @author Rakbow
 * @since 2023-01-11 16:13
 */
@Mapper(componentModel = "spring", uses = MetaVOMapper.class)
public interface BookVOMapper {

    BookVOMapper INSTANCES = Mappers.getMapper(BookVOMapper.class);

    @Mapping(source = "region.labelKey", target = "region.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "region.value", target = "region.value")
    @Mapping(source = "lang.labelKey", target = "lang.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "lang.value", target = "lang.value")
    @Mapping(source = "bookType.labelKey", target = "bookType.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "bookType.value", target = "bookType.value")
    @Mapping(target = "cover", source = "images", qualifiedByName = "getThumbCover")
    @Named("toMiniVO")
    BookMiniVO toMiniVO(Book Book);

    @IterableMapping(qualifiedByName = "toMiniVO")
    List<BookMiniVO> toMiniVO(List<Book> Books);

}
