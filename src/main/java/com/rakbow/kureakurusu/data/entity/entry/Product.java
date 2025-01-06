package com.rakbow.kureakurusu.data.entity.entry;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.ProductType;
import com.rakbow.kureakurusu.data.vo.product.ProductListVO;
import com.rakbow.kureakurusu.data.vo.entry.ProductVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;

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
public class Product extends Entry {

    private Long id;//主键
    @AutoMapping(qualifiedByName = "toAttribute")
    private ProductType type;//作品分类
    private String date;//日期

    public Product() {
        this.id = 0L;
        this.setName("");
        this.setNameZh("");
        this.setNameEn("");
        this.setAliases(new ArrayList<>());
        this.date = "";
        this.type = ProductType.OTHER;
        this.setDetail("");
        this.setRemark("");
        this.setAddedTime(DateHelper.now());
        this.setEditedTime(DateHelper.now());
        this.setStatus(true);
    }
}
