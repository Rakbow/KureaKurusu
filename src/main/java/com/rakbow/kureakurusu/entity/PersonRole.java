package com.rakbow.kureakurusu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-12-10 3:24
 * @Description:
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
