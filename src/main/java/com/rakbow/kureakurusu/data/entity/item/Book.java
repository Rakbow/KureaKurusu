package com.rakbow.kureakurusu.data.entity.item;

import com.rakbow.kureakurusu.data.emun.Language;
import com.rakbow.kureakurusu.data.vo.item.BookListVO;
import com.rakbow.kureakurusu.data.vo.item.BookVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Rakbow
 * @since 2022-10-19 0:26 书籍实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AutoMappers({
        @AutoMapper(target = BookListVO.class, reverseConvertGenerate = false),
        @AutoMapper(target = BookVO.class, reverseConvertGenerate = false)
})
public class Book extends SuperItem {

    private int pages;
    private String size;

    @AutoMapping(qualifiedByName = "toAttribute")
    private Language lang;//语言

    public Book() {
        super();
        this.lang = Language.JAPANESE;
    }
}
