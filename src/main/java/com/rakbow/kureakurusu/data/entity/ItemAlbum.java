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

    private Long id;//表主键
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String catalogNo;//专辑编号
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> publishFormat;//出版形式 在mysql中以数组字符串形式存储
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> albumFormat;//专辑分类 在mysql中以数组字符串形式存储
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> mediaFormat;//媒体类型
    private Boolean hasBonus;//是否包含特典内容 0-无 1-有
    private String bonus;//特典信息

    public ItemAlbum() {
        this.id = 0L;
        this.catalogNo = "";
        this.publishFormat = new ArrayList<>();
        this.albumFormat = new ArrayList<>();
        this.mediaFormat = new ArrayList<>();
        this.hasBonus = false;
        this.bonus = "";
    }
}
