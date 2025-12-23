package com.rakbow.kureakurusu.data.dto;

/**
 * @author Rakbow
 * @since 2024/01/07 0:13
 */
public record UpdateDetailDTO(
        int entityType,
        long entityId,
        String text
) {
}
