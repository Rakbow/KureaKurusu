package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Rakbow
 * @since 2023-12-10 3:24
 */
@Data
@TableName("person_role")
public class PersonRole {

    private Long id;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    @NotBlank(message = "{entity.crud.name_zh.required_field}")
    private String nameZh;
    // @NotBlank(message = "{entity.crud.name_en.required_field}")
    private String nameEn;

    public PersonRole() {
        id = 0L;
        name = "";
        nameZh = "";
        nameEn = "";
    }

}
