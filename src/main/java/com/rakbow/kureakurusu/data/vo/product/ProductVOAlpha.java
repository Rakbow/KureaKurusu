package com.rakbow.kureakurusu.data.vo.product;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * 转换量较少的VO，一般用于list index页面
 *
 * @author Rakbow
 * @since 2023-01-13 9:42
 */
@Data
public class ProductVOAlpha {

    //基础信息
    private int id;//主键
    private String name;//原名
    private String nameZh;//中文译名
    private String nameEn;//英文译名
    private String releaseDate;//发售日期
    private Attribute<Integer> category;//作品分类
    private String remark;//备注

    //关联信息
    private Attribute<Long> franchise;//所属系列

    //审计字段
    private String addedTime;//收录时间
    private String editedTime;//编辑时间
    private boolean status;//状态

}
