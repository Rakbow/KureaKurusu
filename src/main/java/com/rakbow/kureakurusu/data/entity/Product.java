package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.ProductType;
import com.rakbow.kureakurusu.data.entity.common.MetaEntity;
import com.rakbow.kureakurusu.data.vo.product.ProductListVO;
import com.rakbow.kureakurusu.data.vo.product.ProductVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
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
 * @since 2022-08-20 1:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "product", autoResultMap = true)
@AutoMappers({
        @AutoMapper(target = ProductVO.class, reverseConvertGenerate = false),
        @AutoMapper(target = ProductListVO.class, reverseConvertGenerate = false)
})
public class Product extends MetaEntity {

    private Long id;//主键
    @AutoMapping(qualifiedByName = "toAttribute")
    private ProductType type;//作品分类
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;//原名
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameZh;//中文译名
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameEn;//英文译名
    @TableField(typeHandler = StrListHandler.class)
    private List<String> aliases;
    private String date;//日期

    public Product() {
        this.id = 0L;
        this.name = "";
        this.nameZh = "";
        this.nameEn = "";
        this.aliases = new ArrayList<>();
        this.date = "";
        this.type = ProductType.OTHER;
        this.setDetail("");
        this.setRemark("");
        this.setAddedTime(DateHelper.now());
        this.setEditedTime(DateHelper.now());
        this.setStatus(true);
    }
}
