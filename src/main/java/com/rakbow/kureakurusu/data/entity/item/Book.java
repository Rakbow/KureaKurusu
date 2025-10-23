package com.rakbow.kureakurusu.data.entity.item;

import com.rakbow.kureakurusu.data.vo.item.BookListVO;
import com.rakbow.kureakurusu.data.vo.item.BookVO;
import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
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
        @AutoMapper(target = BookListVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class),
        @AutoMapper(target = BookVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
})
public class Book extends SuperItem {

    private int pages;
    private String size;

    // private Language lang;//语言

    public Book() {
        super();
        // this.lang = Language.JAPANESE;
    }
}
