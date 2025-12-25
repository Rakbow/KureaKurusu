package com.rakbow.kureakurusu.data.dto;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/23 16:29
 */
public record ImageDeleteDTO(
        int entityType,
        long entityId,
        List<ImageDeleteMiniDTO> images
) {
}
