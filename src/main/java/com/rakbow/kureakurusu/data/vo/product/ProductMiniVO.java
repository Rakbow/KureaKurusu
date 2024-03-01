package com.rakbow.kureakurusu.data.vo.product;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/3/1 11:30
 */
@Data
public class ProductMiniVO {

    private long id;//主键
    private String cover;
    private String name;//原名
    private String nameZh;//中文译名
    private String nameEn;//英文译名
    private String releaseDate;//发售日期
    private Attribute<Integer> category;//作品分类

}
