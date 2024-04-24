package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.data.emun.Currency;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.handler.ImageHandler;
import com.rakbow.kureakurusu.util.jackson.BooleanToIntDeserializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/4/7 17:29
 */
@Data
@TableName(value = "item", autoResultMap = true)
public class Item {

    private Long id;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameZh;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameEn;
    private Entity type;
    private Long entityId;
    private String ean13;//EAN-13
    private String releaseDate;//发售日期
    private double price;//发行价格
    private Currency currency;//货币单位
    @TableField(typeHandler = ImageHandler.class)
    private List<Image> images;//图片列表
    private String detail;//描述
    private String remark;//备注
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp addedTime;//数据新增时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp editedTime;//数据更新时间
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;//激活状态

    public Item() {
        id = 0L;
        name = "";
        nameZh = "";
        nameEn = "";
        type = Entity.ENTRY;
        entityId = 0L;
        ean13 = "";
        releaseDate = "";
        price = 0;
        currency = Currency.JPY;
        images = new ArrayList<>();
        detail = "";
        remark = "";
        addedTime = DateHelper.now();
        editedTime = DateHelper.now();
        status = true;
    }

    public Item(String name) {
        this.name = name;
        this.editedTime = DateHelper.now();
    }

}
