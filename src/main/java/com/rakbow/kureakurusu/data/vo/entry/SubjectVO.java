package com.rakbow.kureakurusu.data.vo.entry;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/7/2 21:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubjectVO extends EntryVO {

    private Attribute<Integer> type;
    private String date;

}