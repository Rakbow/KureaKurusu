package com.rakbow.kureakurusu.data.entity.resource;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rakbow.kureakurusu.data.common.Constant;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.entity.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Rakbow
 * @since 2024/5/24 10:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@TableName(value = "image", autoResultMap = true)
public class Image extends Entity {

    private Long id;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private int entityType;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long entityId;
    private int type;
    private String name;
    private String nameZh;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private long size;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String url;
    private String detail;

    @TableField(exist = false)
    private String thumb70;//缩略图url(70px)
    @TableField(exist = false)
    private String thumb50;//缩略图url(50px)
    @TableField(exist = false)
    private String thumb;//缩略图url(1200*600)

    public Image() {
        super();
        id = 0L;
        entityType = EntityType.ITEM.getValue();
        entityId = 0L;
        type = ImageType.DEFAULT.getValue();
        name = "";
        nameZh = "";
        url = "";
        size = 0L;
        detail = "";
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
        if (url.contains(Constant.FILE_DOMAIN))
            return url;
        return STR."\{Constant.FILE_DOMAIN}\{url}";
    }

}
