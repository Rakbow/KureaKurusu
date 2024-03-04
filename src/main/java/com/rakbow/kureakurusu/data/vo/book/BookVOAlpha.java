package com.rakbow.kureakurusu.data.vo.book;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.vo.ImageVO;
import lombok.Data;

/**
 * 转换量较少的VO，一般用于list index页面
 *
 * @author Rakbow
 * @since 2023-01-11 10:42
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
    private Attribute<Integer> bookType;//书籍类型 0-未分类 1-小说 2-漫画 3-设定集/原画集/公式书 4-其他
    private String publishDate;//出版日期
    private Attribute<String> region;//地区
    private Attribute<String> lang;//语言
    private double price;//出版价格
    private String currency;//货币单位
    private String summary;//简介
    private boolean hasBonus;//是否包含特典
    private String remark;//备注

    //图片相关
    private ImageVO cover;//封面

    //审计字段
    private String addedTime;//收录时间
    private String editedTime;//编辑时间
    private boolean status;//状态

    //其他
    private long visitNum;//浏览数
}
