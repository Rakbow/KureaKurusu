package com.rakbow.kureakurusu.data.entity;


import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.Currency;
import com.rakbow.kureakurusu.data.emun.ReleaseType;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import com.rakbow.kureakurusu.data.vo.item.AlbumListVO;
import com.rakbow.kureakurusu.data.vo.item.AlbumVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.handler.IntegerListHandler;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-07-19 0:55 专辑实体类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@TableName(value = "album", autoResultMap = true)
@AutoMappers({
        @AutoMapper(target = AlbumListVO.class, reverseConvertGenerate = false),
        @AutoMapper(target = AlbumVO.class, reverseConvertGenerate = false)
})
public class Album extends SuperItem {

    private String catalogNo;

    @AutoMapping(qualifiedByName = "getAlbumFormat")
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> albumFormat;//专辑分类 在mysql中以数组字符串形式存储
    @AutoMapping(qualifiedByName = "getMediaFormat")
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> mediaFormat;//媒体类型

    public Album() {
        super();
        this.catalogNo = "";
        this.albumFormat = new ArrayList<>();
        this.mediaFormat = new ArrayList<>();
    }
}
