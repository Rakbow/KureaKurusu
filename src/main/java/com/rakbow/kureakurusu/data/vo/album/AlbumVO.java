package com.rakbow.kureakurusu.data.vo.album;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * 转换量最大的VO，一般用于详情页面
 *
 * @author Rakbow
 * @since 2023-01-11 10:42
 */
@Data
public class AlbumVO {

    //基础
    private int id;//表主键
    private String catalogNo;//专辑编号
    private String name;//专辑名称（日语）
    private String nameZh;//专辑名称（中文）
    private String nameEn;//专辑名称（英语）
    private String barcode;//商品条形码
    private String releaseDate;//发行日期
    private int price;//发行价格（含税）
    private String currency;//货币单位
    private boolean hasBonus;//是否包含特典内容
    private String remark;//备注

    //规格信息
    private List<Attribute<Integer>> publishFormat;//出版形式 在mysql中以数组字符串形式存储
    private List<Attribute<Integer>> albumFormat;//专辑分类 在mysql中以数组字符串形式存储
    private List<Attribute<Integer>> mediaFormat;//媒体类型

    //大文本字段
    private String bonus;//特典信息

    //音轨相关
    private AlbumTrackInfoVO trackInfo;

    private String addedTime;
    private String editedTime;

    private String detail;
    private boolean status;

}
