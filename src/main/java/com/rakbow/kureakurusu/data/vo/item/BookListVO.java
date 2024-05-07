package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/5/6 11:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BookListVO extends ItemListVO {

    private String isbn10;
    private Attribute<Integer> bookType;//书籍类型 0-未分类 1-小说 2-漫画 3-设定集/原画集/公式书 4-其他
    private Attribute<String> region;//地区
    private Attribute<String> lang;//语言
    private String summary;//简介
    private boolean hasBonus;//是否包含特典

}