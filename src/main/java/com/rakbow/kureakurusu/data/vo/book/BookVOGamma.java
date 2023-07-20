package com.rakbow.kureakurusu.data.vo.book;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.vo.LanguageVO;
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
public class BookVOGamma {

    //基础信息
    private int id;//主键编号
    private String title;//标题（原文）
    private String titleEn;//标题（英文）
    private String titleZh;//标题（中文）
    private String isbn10;//国际标准书号（10位）
    private String isbn13;//国际标准书号（13位）
    private List<String> authors;//作者信息
    private JSONObject bookType;//书籍类型 0-未分类 1-小说 2-漫画 3-设定集/原画集/公式书 4-其他
    private String publishDate;//出版日期
    private RegionVO region;//地区
    private LanguageVO publishLanguage;//语言
    private String summary;//简介
    private boolean hasBonus;//是否包含特典

    //关联信息
    private List<Attribute> franchises;//所属系列
    private JSONArray products;//所属产品

    private String cover;

    private long visitCount;//浏览数
    private long likeCount;//点赞数

}
