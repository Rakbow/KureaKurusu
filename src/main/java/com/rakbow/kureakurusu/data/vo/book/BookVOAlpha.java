package com.rakbow.kureakurusu.data.vo.book;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.vo.LanguageVO;
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
public class BookVOAlpha {

    //基础信息
    private int id;//主键编号
    private String title;//标题（原文）
    private String titleEn;//标题（英文）
    private String titleZh;//标题（中文）
    private String isbn10;//国际标准书号（10位）
    private String isbn13;//国际标准书号（13位）
    private List<String> authors;//作者
    private Attribute bookType;//书籍类型 0-未分类 1-小说 2-漫画 3-设定集/原画集/公式书 4-其他
    private String publishDate;//出版日期
    private RegionVO region;//地区
    private LanguageVO publishLanguage;//语言
    private int price;//出版价格
    private String currencyUnit;//货币单位
    private String summary;//简介
    private boolean hasBonus;//是否包含特典
    private String remark;//备注

    //关联信息
    private List<Attribute> franchises;//所属系列
    private List<Attribute> products;//所属产品

    //图片相关
    private JSONObject cover;//图片（json）

    //审计字段
    private String addedTime;//收录时间
    private String editedTime;//编辑时间
    private boolean status;//状态

    //其他
    private long visitNum;//浏览数
}
