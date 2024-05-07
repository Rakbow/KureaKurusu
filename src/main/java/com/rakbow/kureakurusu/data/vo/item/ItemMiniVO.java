package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/5/7 3:01
 */
@Data
public class ItemMiniVO {

    private long id;
    private String name;
    private String nameZh;
    private String nameEn;
    private Attribute<Integer> type;

}
