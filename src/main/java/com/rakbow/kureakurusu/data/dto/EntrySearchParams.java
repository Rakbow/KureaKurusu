package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.dto.EntitySearchParams;
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

    private int searchType;
    private List<String> keywords;
    private boolean strict;

    public boolean strict() {
        return strict;
    }

}
