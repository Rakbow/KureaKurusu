package com.rakbow.kureakurusu.data.entity.item;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.data.emun.ItemSubType;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.emun.ReleaseType;
import com.rakbow.kureakurusu.data.entity.Entity;
import com.rakbow.kureakurusu.data.vo.item.ItemMiniVO;
import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/4/7 17:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "item", autoResultMap = true)
@NoArgsConstructor
@AutoMappers({
        @AutoMapper(target = ItemMiniVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
})
public class Item extends Entity {

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private ItemType type;
    private ItemSubType subType;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long orgId;

    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameEn;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> aliases;

    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String barcode;// EAN/JAN/ISBN-13
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String catalogId;// album game disc catalog id
    private String releaseDate;
    private double price;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    @AutoMapping(target = "currency", qualifiedByName = "getCurrency")
    private String region;//ISO-3166 region code
    private ReleaseType releaseType;//release type
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean bonus;//has prize

    private String detail;

    private double width;// mm
    private double length;// mm
    private double height;// mm
    private double weight;// g

}
