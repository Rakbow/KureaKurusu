package com.rakbow.kureakurusu.data.vo.disc;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * 转换量较少的VO，一般用于list index页面
 *
 * @author Rakbow
 * @since 2023-01-11 10:42
 */
@Data
public class DiscVOAlpha {

    //基础信息
    private int id;//主键
    private String catalogNo;//商品型番
    private String name;//商品名(原语言)
    private String nameZh;//商品译名(中)
    private String nameEn;//商品译名(英)
    private String barcode;//商品条形码
    private Attribute<String> region;//地区
    private String releaseDate;//发行日期
    private int price;//发行价格
    private String currencyUnit;//货币单位
    private boolean limited;//发售版本是否为限定版 0-否 1-是
    private boolean hasBonus;//是否包含特典
    private String remark;//备注

    //关联信息
    private List<Attribute<Integer>> franchises;//所属系列id

    //复杂字段
    private List<Attribute<Integer>> mediaFormat;//媒体格式 0-未分类 1-DVD 2-Blu-ray

    //审计字段
    private String addedTime;//收录时间
    private String editedTime;//编辑时间
    private boolean status;//状态

    private long visitNum;//浏览量

}
