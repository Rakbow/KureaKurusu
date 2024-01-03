package com.rakbow.kureakurusu.data.vo.person;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2023-11-05 23:23
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
