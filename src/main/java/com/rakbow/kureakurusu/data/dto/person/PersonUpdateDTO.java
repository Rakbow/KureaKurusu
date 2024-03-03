package com.rakbow.kureakurusu.data.dto.person;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.dto.base.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("person")
public class PersonUpdateDTO extends DTO {

    private long id;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    private String nameEn;
    private List<String> aliases;
    private int gender;
    private String birthDate;
    private String remark;
    private String addedTime;
    private String editedTime;

}
