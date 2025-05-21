package com.rakbow.kureakurusu.data.vo.entry;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/5/21 9:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PersonListVO extends EntryListVO {

    private String birthDate;
    private Attribute<Integer> gender;

}
