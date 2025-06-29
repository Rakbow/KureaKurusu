package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2023-12-10 3:24
 */
@Data
@NoArgsConstructor
@TableName("role")
public class Role {

    @OrderBy
    private Long id;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    // @NotBlank(message = "{entity.crud.name_en.required_field}")
    private String nameEn;
    @JsonIgnore
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;//激活状态

    @TableField(exist = false)
    private int count;

}
