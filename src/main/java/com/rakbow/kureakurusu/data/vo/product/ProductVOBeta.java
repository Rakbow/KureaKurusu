package com.rakbow.kureakurusu.data.vo.product;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-02-03 16:13
 * @Description: VO 存储到搜索引擎数据库
 */
@Data
public class ProductVOBeta {

    //基础信息
    private int id;//主键
    private String name;//原名
    private String nameZh;//中文译名
    private String nameEn;//英文译名
    private String releaseDate;//发售日期
    private Attribute<Integer> category;//作品分类

    //关联信息
    private Attribute<Integer> franchise;//所属系列

    private String cover;

}
