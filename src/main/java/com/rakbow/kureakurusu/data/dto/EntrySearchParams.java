package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/1/20 3:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntrySearchParams extends EntitySearchParams {

    private Integer type;
    private List<String> keywords;
    private boolean strict;
    private String sortFiled;
    private int sortOrder;
    private int mode;//search mode: 0-normal 1-manager

    public boolean strict() {
        return strict;
    }

}
