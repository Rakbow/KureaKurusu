package com.rakbow.kureakurusu.data.vo.person;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-11-05 23:23
 * @Description:
 */
@Data
@TableName("person")
public class PersonVOBeta {

    private long id;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    private String nameEn;
    private List<String> aliases;
    private Attribute<Integer> gender;
    private String birthDate;
    private String remark;
    private String addedTime;
    private String editedTime;
    private int status;

}
