package com.rakbow.kureakurusu.entity;

import com.rakbow.kureakurusu.entity.common.MetaEntity;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-08-20 1:43
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Product extends MetaEntity {

    private Integer id;//主键
    private String name;//原名
    private String nameZh;//中文译名
    private String nameEn;//英文译名
    private Date releaseDate;//发售日期
    private int franchise;//所属系列id
    private int category;//作品分类
    private String organizations;//相关组织
    private String staffs;//staff

    public Product() {
        this.id = 0;
        this.name = "";
        this.nameZh = "";
        this.nameEn = "";
        this.releaseDate = null;
        this.franchise = 0;
        this.category = 0;
        this.organizations = "[]";
        this.staffs = "[]";
        this.setImages("[]");
        this.setDescription("");
        this.setRemark("");
        this.setAddedTime(DateHelper.NOW_TIMESTAMP);
        this.setEditedTime(DateHelper.NOW_TIMESTAMP);
        this.setStatus(1);
    }
}
