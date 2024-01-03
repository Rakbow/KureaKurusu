package com.rakbow.kureakurusu.data.vo.person;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2023-10-06 5:51
 */
@Data
public class PersonMiniVO {

    private long id;
    private String name;
    private String nameZh;
    private String nameEn;
    private String cover;
    private Attribute<Integer> gender;
    private String birthDate;

}
