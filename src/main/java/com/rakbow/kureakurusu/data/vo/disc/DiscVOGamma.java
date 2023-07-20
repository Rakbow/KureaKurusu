package com.rakbow.kureakurusu.data.vo.disc;

import com.alibaba.fastjson2.JSONArray;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.vo.RegionVO;
import lombok.Data;

import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-02-03 16:13
 * @Description: VO 存储到搜索引擎数据库
 */
@Data
public class DiscVOGamma {

    //基础信息
    private int id;//主键
    private String catalogNo;//商品型番
    private String name;//商品名(原语言)
    private String nameZh;//商品译名(中)
    private String nameEn;//商品译名(英)
    private RegionVO region;//地区
    private String releaseDate;//发行日期
    private boolean limited;//发售版本是否为限定版 0-否 1-是
    private boolean hasBonus;//是否包含特典

    //关联信息
    private List<Attribute> franchises;//所属系列id
    private List<Attribute> products;//所属作品id

    private JSONArray mediaFormat;//媒体格式 0-未分类 1-DVD 2-Blu-ray

    private String cover;

    private long visitCount;//浏览数
    private long likeCount;//点赞数

}
