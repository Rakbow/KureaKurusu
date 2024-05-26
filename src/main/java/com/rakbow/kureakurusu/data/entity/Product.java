package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.dto.ProductUpdateDTO;
import com.rakbow.kureakurusu.data.emun.ProductCategory;
import com.rakbow.kureakurusu.data.entity.common.MetaEntity;
import com.rakbow.kureakurusu.data.vo.product.ProductListVO;
import com.rakbow.kureakurusu.data.vo.product.ProductVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;//原名
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameZh;//中文译名
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameEn;//英文译名
    private String releaseDate;//日期
    @AutoMapping(qualifiedByName = "getFranchise")
    private long franchise;//所属系列id
    @AutoMapping(qualifiedByName = "toAttribute")
    private ProductCategory category;//作品分类

    public Product() {
        this.id = 0L;
        this.name = "";
        this.nameZh = "";
        this.nameEn = "";
        this.releaseDate = "";
        this.franchise = 0L;
        this.category = ProductCategory.MISC;
        this.setDetail("");
        this.setRemark("");
        this.setAddedTime(DateHelper.now());
        this.setEditedTime(DateHelper.now());
        this.setStatus(true);
    }

    public Product(ProductUpdateDTO dto) {
        id = dto.getId();
        name = dto.getName();
        nameZh = dto.getNameZh();
        nameEn = dto.getNameEn();
        releaseDate = dto.getReleaseDate();
        franchise = dto.getFranchise();
        category = ProductCategory.get(dto.getCategory());
        setRemark(dto.getRemark());
        updateEditedTime();
    }
}
