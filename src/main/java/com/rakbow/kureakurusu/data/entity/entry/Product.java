package com.rakbow.kureakurusu.data.entity.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.ProductType;
import com.rakbow.kureakurusu.data.vo.entry.ProductListVO;
import com.rakbow.kureakurusu.data.vo.entry.ProductVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2022-08-20 1:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "product", autoResultMap = true)
@NoArgsConstructor
@AutoMappers({
        @AutoMapper(target = ProductVO.class, reverseConvertGenerate = false),
        @AutoMapper(target = ProductListVO.class, reverseConvertGenerate = false)
})
public class Product extends Entry {

    @TableId(type = IdType.AUTO)
    private Long id = 0L;
    @AutoMapping(qualifiedByName = "toAttribute")
    private ProductType type;//作品分类
    private String date;//日期
}
