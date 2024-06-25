package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.emun.ReleaseType;
import com.rakbow.kureakurusu.data.vo.item.ItemMiniVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.handler.IntegerListHandler;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/4/7 17:29
 */
@Data
@TableName(value = "item", autoResultMap = true)
@NoArgsConstructor
@AutoMappers({
        @AutoMapper(target = ItemMiniVO.class, reverseConvertGenerate = false)
})
public class Item {

    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @AutoMapping(qualifiedByName = "toAttribute")
    private ItemType type;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long orgId;

    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> aliases;

    private String barcode;// EAN/JAN/ISBN-13
    private String catalogId;// album game disc catalog id
    private String releaseDate;
    private double price;
    private String region;//ISO-3166 region code
    private ReleaseType releaseType;//release type
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean bonus;//has prize

    private String detail;

    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> dimensions;// W L H mm
    private int weight;// g

    private String remark;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp addedTime = DateHelper.now();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp editedTime = DateHelper.now();

    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;

}
