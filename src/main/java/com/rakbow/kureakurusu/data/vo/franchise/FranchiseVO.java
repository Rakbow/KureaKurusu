package com.rakbow.kureakurusu.data.vo.franchise;

import com.alibaba.fastjson2.JSONArray;
import lombok.Data;

import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-01-11 10:42
 * @Description: 转换量最大的VO，一般用于详情页面
 */
@Data
public class FranchiseVO {

    //基础信息
    private int id;//主键
    private String name;//系列名
    private String nameZh;//系列名（中文）
    private String nameEn;//系列名（英语）
    private String originDate;//发行日期
    private String remark;//备注

    private boolean metaLabel;//是否为meta-franchise
    private JSONArray childFranchiseInfos;//子系列
    private List<Integer> childFranchises;//子系列ids
    private ParentFranchiseVO parentFranchise;//上级元系列

}
