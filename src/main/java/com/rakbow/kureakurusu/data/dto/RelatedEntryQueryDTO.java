package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/12/30 20:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatedEntryQueryDTO {

    private int entityType;
    private long entityId;

    private List<List<Integer>> entryTypeSets;

    private int size;

}
