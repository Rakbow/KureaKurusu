package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.data.emun.Currency;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.vo.item.ItemMiniVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameZh;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameEn;

    private String ean13;//EAN-13
    private String releaseDate;//发售日期
    private double price;//发行价格
    private Currency currency;//货币单位

    private String detail;//描述
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean hasBonus;//是否包含特典内容 0-无 1-有
    private String bonus;//特典信息
    private String remark;//备注

    @TableField(updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp addedTime = DateHelper.now();//数据新增时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp editedTime = DateHelper.now();//数据更新时间

    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;//激活状态

}
