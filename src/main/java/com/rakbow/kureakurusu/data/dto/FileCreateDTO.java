package com.rakbow.kureakurusu.data.dto;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/6/12 14:49
 */
public record FileCreateDTO(int entityType, long entityId, List<Long> fileIds) {
}
