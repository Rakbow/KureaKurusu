package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.dto.FranchiseUpdateDTO;
import com.rakbow.kureakurusu.data.entity.common.MetaEntity;
import com.rakbow.kureakurusu.data.vo.franchise.FranchiseVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;

/**
 * @author Rakbow
 * @since 2022-08-20 0:50 系列实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "franchise", autoResultMap = true)
@AutoMappers({
        @AutoMapper(target = FranchiseVO.class, reverseConvertGenerate = false)
})
public class Franchise extends MetaEntity {

    private Long id;//主键
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;//系列名
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameZh;//系列名（中文）
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
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
        this.setStatus(true);
    }

    public Franchise(FranchiseUpdateDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.nameZh = dto.getNameZh();
        this.nameEn = dto.getNameEn();
        updateEditedTime();
    }

}
