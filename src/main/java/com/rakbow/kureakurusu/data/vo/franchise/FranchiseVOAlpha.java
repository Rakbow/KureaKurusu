package com.rakbow.kureakurusu.data.vo.franchise;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-01-11 10:42
 * @Description: 转换量较少的VO，一般用于list index页面
 */
@Data
public class FranchiseVOAlpha {

    //基础信息
    private int id;//主键
    private String name;//系列名
    private String nameZh;//系列名（中文）
    private String nameEn;//系列名（英语）
    private String originDate;//发行日期
    private String remark;//备注

    //图片
    private JSONObject cover;//封面/logo

    //审计字段
    private String addedTime;//创建时间
    private String editedTime;//更新时间
    private boolean status;//状态

    //meta相关数据
    private boolean metaLabel;//是否为meta-franchise
    private List<Integer> childFranchises;//子系列
    private int metaId;//上级元系列id


}
