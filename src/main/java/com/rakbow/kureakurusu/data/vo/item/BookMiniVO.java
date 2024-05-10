package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/3/4 14:01
 */
@Data
public class BookMiniVO {

    private long id;
    private String title;
    private String titleZh;
    private String titleEn;
    private String isbn10;
    private String isbn13;
    private String cover;
    private Attribute<Integer> bookType;
    private Attribute<String> region;
    private Attribute<String> lang;
    private String publishDate;
    private boolean hasBonus;//是否包含特典

}
