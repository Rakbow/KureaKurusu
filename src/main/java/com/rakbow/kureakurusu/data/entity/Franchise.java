package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.entity.common.MetaEntity;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Rakbow
 * @since 2022-08-20 0:50 系列实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "franchise", autoResultMap = true)
public class Franchise extends MetaEntity {

    private Long id;//主键
    private String name;//系列名
    private String nameZh;//系列名（中文）
    private String nameEn;//系列名（英语）

    public Franchise() {
        this.id = 0L;
        this.name = "";
        this.nameZh = "";
        this.nameEn = "";
        this.setAddedTime(DateHelper.now());
        this.setEditedTime(DateHelper.now());
        this.setDetail("");
        this.setRemark("");
        this.setImages(new ArrayList<>());
        this.setStatus(1);
    }

}
