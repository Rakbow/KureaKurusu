package com.rakbow.kureakurusu.data.image;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

/**
 * @author Rakbow
 * @since 2024/5/24 10:46
 */
@Data
@ToString(callSuper = true)
@TableName(value = "image", autoResultMap = true)
public class Image {

    private Long id;
    private EntityType entityType;
    private long entityId;
    private ImageType type;
    private String name;
    private String nameZh;
    private String url;
    private String detail;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp addedTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp editedTime;
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;//激活状态

    public Image() {
        id = 0L;
        entityType = EntityType.ITEM;
        entityId = 0L;
        type = ImageType.DEFAULT;
        name = "";
        nameZh = "";
        url = "";
        detail = "";
        addedTime = DateHelper.now();
        editedTime = DateHelper.now();
        status = Boolean.TRUE;
    }

    @JsonIgnore
    public boolean isMain() {
        return this.type == ImageType.MAIN;
    }

    @JsonIgnore
    public boolean isDisplay() {
        return this.type != ImageType.DEFAULT;
    }

}
