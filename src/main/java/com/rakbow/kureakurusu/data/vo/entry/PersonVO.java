package com.rakbow.kureakurusu.data.vo.entry;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2023-11-07 23:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PersonVO extends EntryVO {

    private String birthDate;
    private Attribute<Integer> gender;

}
