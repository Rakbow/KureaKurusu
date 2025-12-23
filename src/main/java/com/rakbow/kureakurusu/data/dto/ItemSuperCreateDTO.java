package com.rakbow.kureakurusu.data.dto;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/1/4 17:00
 */
public record ItemSuperCreateDTO(ItemCreateDTO item, List<ImageMiniDTO> images,
                                 List<RelatedEntityMiniDTO> relatedEntries, Boolean generateThumb) {
}
