package com.rakbow.kureakurusu.data.dto;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/12/30 20:11
 */
public record RelatedEntryQueryDTO(
        int entityType,
        long entityId,
        List<List<Integer>> entryTypeSets,
        int size
) {
}
