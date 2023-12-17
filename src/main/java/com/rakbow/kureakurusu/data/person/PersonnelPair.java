package com.rakbow.kureakurusu.data.person;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-12-17 22:25
 * @Description:
 */
@Data
public class PersonnelPair {

    private Attribute<Long> role;
    private Attribute<Long> person;

}
