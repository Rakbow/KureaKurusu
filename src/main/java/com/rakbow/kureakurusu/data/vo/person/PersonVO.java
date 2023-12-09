package com.rakbow.kureakurusu.data.vo.person;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-11-07 23:45
 * @Description:
 */
@Data
public class PersonVO {

    private long id;
    private String name;
    private String nameZh;
    private String nameEn;
    private String cover;
    private String birthDate;
    private Attribute<Integer> gender;
    private List<String> aliases;
    private String detail;
    private String remark;
    private Boolean status;

}
