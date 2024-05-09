package com.rakbow.kureakurusu.data.vo.book;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.vo.item.ItemVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 转换量最大的VO，一般用于详情页面
 *
 * @author Rakbow
 * @since 2023-01-11 10:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BookVO extends ItemVO {

    private String isbn10;
    private Attribute<Integer> bookType;
    private Attribute<String> region;
    private Attribute<String> lang;
    private String summary;

}
