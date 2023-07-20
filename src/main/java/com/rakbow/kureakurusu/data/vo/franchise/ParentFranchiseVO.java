package com.rakbow.kureakurusu.data.vo.franchise;

import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-02-09 0:57
 * @Description:
 */
@Data
public class ParentFranchiseVO {

    //基础信息
    private int id;//主键
    private String name;//系列名
    private String nameZh;//系列名（中文）
    private String nameEn;//系列名（英语）

}
