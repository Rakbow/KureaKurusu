package com.rakbow.kureakurusu.data.vo.merch;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.vo.RegionVO;
import lombok.Data;

import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-01-11 10:42
 * @Description: 转换量较少的VO，一般用于list index页面
 */
@Data
public class MerchVOAlpha {

    //基础信息
    private int id;//主键编号
    private String name;//商品名（原文）
    private String nameZh;//商品名（中文）
    private String nameEn;//商品名（英文）
    private String barcode;//商品条形码
    private String releaseDate;//发售日
    private int price;//价格
    private String currencyUnit;//价格单位
    private boolean notForSale;//是否非卖品
    private String remark;//备注

    //关联信息
    private List<Attribute<Integer>> franchises;//所属系列
    private List<Attribute<Integer>> products;//所属产品

    //复杂字段
    private Attribute<Integer> category;//商品分类
    private RegionVO region;//地区

    //图片
    private JSONObject cover;

    //审计字段
    private String addedTime;//收录时间
    private String editedTime;//编辑时间
    private boolean status;//状态

    //其他字段
    private long visitNum;//浏览数

}
