package com.rakbow.kureakurusu.data.entity.item;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.Language;
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

    private String summary;//简介
    private Language lang;//语言

    private int pages;
    private String size;

}
