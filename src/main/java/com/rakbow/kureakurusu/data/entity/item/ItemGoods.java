package com.rakbow.kureakurusu.data.entity.item;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/7/12 15:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@TableName(value = "item_goods", autoResultMap = true)
public class ItemGoods extends SubItem {

    private Long id;
    private String scale;
    private String various;
    private String title;
    private String titleEn;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> versions;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> versionsEn;

}
