package com.rakbow.kureakurusu.data.vo.person;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-11-05 23:23
 * @Description:
 */
@Data
public class PersonVOBeta {

    private long id;
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
