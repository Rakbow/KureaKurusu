package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.common.Constant;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/5/7 3:01
 */
@Data
public class ItemMiniVO {

    private long id;
    private String name;
    private String nameEn;
    private List<String> aliases;
    private Attribute<Integer> type;
    private String thumb;
    private String cover;
    private String releaseDate;
    private double price;
    private String currency;

}
