package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.util.jackson.BooleanToIntDeserializer;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Rakbow
 * @since 2023-12-10 3:24
 */
@Data
@TableName("person_role")
public class PersonRole {

    @OrderBy
    private Long id;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    @NotBlank(message = "{entity.crud.name_zh.required_field}")
    private String nameZh;
    // @NotBlank(message = "{entity.crud.name_en.required_field}")
    private String nameEn;
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;//激活状态

    public PersonRole() {
        id = 0L;
        name = "";
        nameZh = "";
        nameEn = "";
    }

}
