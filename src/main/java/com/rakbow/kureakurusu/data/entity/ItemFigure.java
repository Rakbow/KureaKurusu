package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.FigureType;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/7/26 17:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@TableName(value = "item_figure", autoResultMap = true)
public class ItemFigure extends SubItem {

    private Long id;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private FigureType figureType;
    private String scale;
    private String various;
    private String title;
    private String titleEn;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> versions;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> versionsEn;

}
