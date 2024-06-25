package com.rakbow.kureakurusu.data.image;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.data.common.Constant;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

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
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private EntityType entityType;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private long entityId;
    private int type;
    private String name;
    private String nameZh;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String url;
    private String detail;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp addedTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp editedTime;
    @JsonIgnore
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;

    @TableField(exist = false)
    private String thumbUrl70;//缩略图url(70px)
    @TableField(exist = false)
    private String thumbUrl50;//缩略图url(50px)
    @TableField(exist = false)
    private String blackUrl;//缩略图url(黑背景)

    public Image() {
        id = 0L;
        entityType = EntityType.ITEM;
        entityId = 0L;
        type = ImageType.DEFAULT.getValue();
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
        return this.type == ImageType.MAIN.getValue();
    }

    @JsonIgnore
    public boolean isDisplay() {
        return this.type != ImageType.OTHER.getValue();
    }

    public String getUrl() {
        if(StringUtils.isBlank(this.url)) return Constant.EMPTY;
        return STR."\{Constant.FILE_DOMAIN}\{this.url}";
    }

}
