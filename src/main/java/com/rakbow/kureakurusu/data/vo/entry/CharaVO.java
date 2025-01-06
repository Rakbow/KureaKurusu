package com.rakbow.kureakurusu.data.vo.entry;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/12/28 4:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CharaVO extends EntryVO {

    private String birthDate;
    private Attribute<Integer> gender;

}
