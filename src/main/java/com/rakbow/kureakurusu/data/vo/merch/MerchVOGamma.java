package com.rakbow.kureakurusu.data.vo.merch;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.vo.RegionVO;
import lombok.Data;

import java.util.List;

/**
 * VO 存储到搜索引擎数据库
 *
 * @author Rakbow
 * @since 2023-02-03 16:13
 */
@Data
public class MerchVOGamma {

    //基础信息
    private int id;//主键编号
    private String name;//商品名（原文）
    private String nameZh;//商品名（中文）
    private String nameEn;//商品名（英文）
    private String releaseDate;//发售日
    private boolean notForSale;//是否非卖品
    //关联信息
    private List<Attribute<Integer>> franchises;//所属系列
    private List<Attribute<Integer>> products;//所属产品

    //复杂字段
    private Attribute<Integer> category;//商品分类
    private RegionVO region;//地区

    private String cover;

    private long visitCount;//浏览数
    private long likeCount;//点赞数


}
