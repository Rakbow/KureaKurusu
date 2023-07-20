package com.rakbow.kureakurusu.data.vo.disc;

import com.alibaba.fastjson2.JSONArray;
import com.rakbow.kureakurusu.data.vo.RegionVO;
import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-01-11 10:42
 * @Description: 转换量最大的VO，一般用于详情页面
 */
@Data
public class DiscVO {

    //基础信息
    private int id;//主键
    private String catalogNo;//商品型番
    private String name;//商品名(原语言)
    private String nameZh;//商品译名(中)
    private String nameEn;//商品译名(英)
    private String barcode;//商品条形码
    private RegionVO region;//地区
    private String releaseDate;//发行日期
    private int price;//发行价格
    private String currencyUnit;//货币单位
    private boolean limited;//发售版本是否为限定版 0-否 1-是
    private boolean hasBonus;//是否包含特典
    private String remark;//备注

    //复杂字段
    private JSONArray mediaFormat;//媒体格式 0-未分类 1-DVD 2-Blu-ray
    private JSONArray spec;//商品规格
    private String bonus;//特典信息

}
