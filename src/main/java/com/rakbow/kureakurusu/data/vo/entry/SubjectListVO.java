package com.rakbow.kureakurusu.data.vo.entry;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/5/21 10:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubjectListVO extends EntryListVO {

    private String date;
    private Attribute<Integer> type;

}
