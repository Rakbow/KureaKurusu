package com.rakbow.kureakurusu.data.dto;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/17 16:28
 */
public record EntrySuperCreateDTO(
        EntryCreateDTO entry,
        List<ImageMiniDTO> images,
        List<RelatedEntityMiniDTO> relatedEntries
) {
}
