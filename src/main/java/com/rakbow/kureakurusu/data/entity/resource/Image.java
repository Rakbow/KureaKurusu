package com.rakbow.kureakurusu.data.entity.resource;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rakbow.kureakurusu.data.common.Constant;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.entity.Entity;
import com.rakbow.kureakurusu.data.vo.resource.ImageVO;
import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import io.github.linpeilie.annotations.AutoMappings;
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
@AutoMappers({
        @AutoMapper(target = ImageVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
})
public class Image extends Entity {

    private Long id;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private int entityType;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long entityId;
    @AutoMapping(qualifiedByName = "toAttribute")
    private ImageType type;
    private String name;
    private String nameZh;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @AutoMapping(qualifiedByName = "size")
    private long size;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @AutoMappings({
            @AutoMapping(target = "thumb", qualifiedByName = "thumbImage"),
            @AutoMapping(target = "display", qualifiedByName = "displayImage")
    })
    private String url;
    private String detail;

    public Image() {
        super();
        id = 0L;
        entityType = EntityType.ITEM.getValue();
        entityId = 0L;
        type = ImageType.DEFAULT;
        name = "";
        nameZh = "";
        url = "";
        size = 0L;
        detail = "";
    }

    @JsonIgnore
    public boolean isMain() {
        return this.type == ImageType.MAIN;
    }

    public String getUrl() {
        if (url.contains(Constant.FILE_DOMAIN))
            return url;
        return STR."\{Constant.FILE_DOMAIN}\{url}";
    }

}
