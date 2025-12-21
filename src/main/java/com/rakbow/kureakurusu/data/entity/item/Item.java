package com.rakbow.kureakurusu.data.entity.item;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.data.enums.ItemSubType;
import com.rakbow.kureakurusu.data.enums.ItemType;
import com.rakbow.kureakurusu.data.enums.ReleaseType;
import com.rakbow.kureakurusu.data.entity.Entity;
import com.rakbow.kureakurusu.data.vo.item.ItemSearchVO;
import com.rakbow.kureakurusu.data.vo.item.ItemVO;
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
        @AutoMapper(target = ItemSearchVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class),
        @AutoMapper(target = ItemVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
})
public class Item extends Entity {

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private ItemType type;
    private ItemSubType subType;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private long orgId;// myFigureCollection id

    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameEn;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> aliases;

    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String barcode;// EAN/JAN/ISBN-13
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String catalogId;// album/game/video catalog id
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

    //album video
    private int discs;//total disc number of album
    private int episodes;//episode number of album and video
    private int runTime;//total running time of album
    //book
    private int pages;//total page number of book

    //figure goods
    private String various;
    private String scale;
    private String title;
    private String titleEn;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> versions;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> versionsEn;

    @TableLogic
    private int del;

}
