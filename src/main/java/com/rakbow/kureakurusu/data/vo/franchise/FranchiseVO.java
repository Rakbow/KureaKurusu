package com.rakbow.kureakurusu.data.vo.franchise;

import lombok.Data;

/**
 * 转换量最大的VO，一般用于详情页面
 *
 * @author Rakbow
 * @since 2023-01-11 10:42
 */
@Data
public class FranchiseVO {

    //基础信息
    private int id;//主键
    private String name;//系列名
    private String nameZh;//系列名（中文）
    private String nameEn;//系列名（英语）
    private String remark;//备注

    private String addedTime;
    private String editedTime;

    private String detail;
    private boolean status;

}
