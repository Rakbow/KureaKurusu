package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rakbow.kureakurusu.data.emun.GoodsType;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import com.rakbow.kureakurusu.data.vo.item.GoodsListVO;
import com.rakbow.kureakurusu.data.vo.item.GoodsVO;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
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
 * @since 2023-01-04 10:26 周边实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AutoMappers({
        @AutoMapper(target = GoodsListVO.class, reverseConvertGenerate = false),
        @AutoMapper(target = GoodsVO.class, reverseConvertGenerate = false)
})
public class Goods extends SuperItem {

    @AutoMapping(qualifiedByName = "toAttribute")
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private GoodsType goodsType;
    private String scale;
    private String various;
    private String title;
    private String titleEn;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> versions;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> versionsEn;

    public Goods() {
        super();
        goodsType = GoodsType.MISC;
        scale = "";
        various = "";
        title = "";
        titleEn = "";
        versions = new ArrayList<>();
        versionsEn = new ArrayList<>();
    }

}
