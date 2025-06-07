package com.rakbow.kureakurusu.data.entity.item;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.toolkit.handler.IntegerListHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/6/6 18:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "item_disc", autoResultMap = true)
public class ItemDisc extends SubItem {

    private Long id;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private int discs;
    private int episodes;
    private int runTime;
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> mediaFormat;//media format

}
