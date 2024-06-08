package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.BookType;
import com.rakbow.kureakurusu.data.emun.Currency;
import com.rakbow.kureakurusu.data.emun.Language;
import com.rakbow.kureakurusu.data.emun.Region;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import com.rakbow.kureakurusu.data.vo.item.BookListVO;
import com.rakbow.kureakurusu.data.vo.item.BookVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
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
@TableName(value = "book", autoResultMap = true)
@AutoMappers({
        @AutoMapper(target = BookListVO.class, reverseConvertGenerate = false),
        @AutoMapper(target = BookVO.class, reverseConvertGenerate = false)
})
public class Book extends SuperItem {

    @TableField(value = "isbn_10", whereStrategy = FieldStrategy.NOT_EMPTY)
    private String isbn10;

    @AutoMapping(qualifiedByName = "toAttribute")
    private BookType bookType;//书籍类型

    private String authors;//作者（译者，插画，原作者等，json）

    @AutoMapping(qualifiedByName = "toAttribute")
    private Language lang;//语言
    private String summary;//简介
    private String spec;//规格

    public Book() {
        super();
        this.isbn10 = "";
        this.bookType = BookType.OTHER;
        this.lang = Language.JAPANESE;
        this.authors = "[]";
        this.summary = "";
        this.spec = "[]";
    }
}
