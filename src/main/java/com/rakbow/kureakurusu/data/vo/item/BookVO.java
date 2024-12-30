package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.Attribute;
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

    private int pages;
    private String size;
    private Attribute<String> lang;
    private String summary;

}
