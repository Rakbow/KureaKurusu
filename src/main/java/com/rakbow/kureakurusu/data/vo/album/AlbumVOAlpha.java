package com.rakbow.kureakurusu.data.vo.album;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.vo.ImageVO;
import lombok.Data;

import java.util.List;

/**
 * 转换量较少的VO，一般用于list index页面
 *
 * @author Rakbow
 * @since 2023-01-11 10:42
 */
@Data
public class AlbumVOAlpha {

    //基础信息
    private long id;
    private String catalogNo;//专辑编号
    private String name;//专辑名称
    private String nameZh;//专辑名称（中文）
    private String nameEn;//专辑名称（英语）
    private String barcode;//商品条形码
    private String releaseDate;//发行日期
    private double price;//发行价格（含税）
    private String currency;//货币单位
    private boolean hasBonus;//是否包含特典内容
    private String remark;//备注

    //图片相关
    private ImageVO cover;//封面

    //规格信息
    private List<Attribute<Integer>> publishFormat;//出版形式 在mysql中以数组字符串形式存储
    private List<Attribute<Integer>> albumFormat;//专辑分类 在mysql中以数组字符串形式存储
    private List<Attribute<Integer>> mediaFormat;//媒体类型

    //审计字段
    private String addedTime;//数据新增时间
    private String editedTime;//数据更新时间
    private boolean status;//激活状态

    //其他字段
    private long visitNum;


}
