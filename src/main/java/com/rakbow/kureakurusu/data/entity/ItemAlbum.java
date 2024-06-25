package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.toolkit.handler.IntegerListHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/4/8 18:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "item_album", autoResultMap = true)
public class ItemAlbum extends SubItem {

    private Long id;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private int discs;
    private int tracks;
    private int runTime;
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> albumFormat;//album type
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> mediaFormat;//media format

}
