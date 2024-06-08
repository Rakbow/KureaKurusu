package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.BookType;
import com.rakbow.kureakurusu.data.emun.Language;
import com.rakbow.kureakurusu.data.emun.Region;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Rakbow
 * @since 2024/4/23 16:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@TableName(value = "item_book", autoResultMap = true)
public class ItemBook extends SubItem {

    private Long id;//主键编号
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String isbn10;//国际标准书号（10位）
    private BookType bookType;//书籍类型

    private String authors;//作者（译者，插画，原作者等，json）

    private Language lang;//语言

    private String summary;//简介
    private String spec;//规格

}
