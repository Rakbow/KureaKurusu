package com.rakbow.kureakurusu.data.vo.product;

import com.alibaba.fastjson2.JSONArray;
import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * 转换量最大的VO，一般用于详情页面
 *
 * @author Rakbow
 * @since 2023-01-13 9:42
 */
@Data
public class ProductVO {

    //基础信息
    private int id;//主键
    private String name;//原名
    private String nameZh;//中文译名
    private String nameEn;//英文译名
    private String releaseDate;//发售日期
    private Attribute<Integer> category;//作品分类
    private String remark;//备注

    private Attribute<Integer> franchise;//所属系列
    private String addedTime;
    private String editedTime;

    private String detail;
    private boolean status;

}
