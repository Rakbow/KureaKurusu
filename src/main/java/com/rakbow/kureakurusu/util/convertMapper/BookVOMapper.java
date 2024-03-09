package com.rakbow.kureakurusu.util.convertMapper;

import com.rakbow.kureakurusu.annotation.ToVO;
import com.rakbow.kureakurusu.data.dto.book.BookAddDTO;
import com.rakbow.kureakurusu.data.emun.entity.book.BookType;
import com.rakbow.kureakurusu.data.entity.Book;
import com.rakbow.kureakurusu.data.vo.book.BookMiniVO;
import com.rakbow.kureakurusu.data.vo.book.BookVO;
import com.rakbow.kureakurusu.data.vo.book.BookVOAlpha;
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
@Mapper(componentModel = "spring")
public interface BookVOMapper extends MetaVOMapper {

    BookVOMapper INSTANCES = Mappers.getMapper(BookVOMapper.class);

    @ToVO
    @Mapping(source = "currency.value", target = "currency")
    @Mapping(source = "region.labelKey", target = "region.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "region.value", target = "region.value")
    @Mapping(source = "lang.labelKey", target = "lang.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "lang.value", target = "lang.value")
    @Mapping(source = "bookType.labelKey", target = "bookType.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "bookType.value", target = "bookType.value")
    @Named("toVO")
    BookVO toVO(Book book);

    @Mapping(source = "region", target = "region", qualifiedByName = "getRegion")
    @Mapping(source = "lang", target = "lang", qualifiedByName = "getLanguage")
    @Mapping(source = "bookType", target = "bookType", qualifiedByName = "getBookType")
    @Mapping(source = "currency", target = "currency", qualifiedByName = "getCurrency")
    @Named("build")
    Book build(BookAddDTO dto);

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

    @Mapping(source = "currency.value", target = "currency")
    @Mapping(source = "region.labelKey", target = "region.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "region.value", target = "region.value")
    @Mapping(source = "lang.labelKey", target = "lang.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "lang.value", target = "lang.value")
    @Mapping(source = "bookType.labelKey", target = "bookType.label",qualifiedByName = "getEnumLabel")
    @Mapping(source = "bookType.value", target = "bookType.value")
    @Mapping(target = "cover", ignore = true)
    @Mapping(target = "visitNum", ignore = true)
    @ToVO
    @Named("toVOAlpha")
    BookVOAlpha toVOAlpha(Book book);

    @IterableMapping(qualifiedByName = "toVOAlpha")
    List<BookVOAlpha> toVOAlpha(List<Book> books);

    @Named("getBookType")
    default BookType getBookType(int value) {
        return BookType.get(value);
    }

}
