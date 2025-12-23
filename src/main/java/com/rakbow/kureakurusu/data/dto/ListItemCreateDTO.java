package com.rakbow.kureakurusu.data.dto;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/24 22:20
 */
public record ListItemCreateDTO(long listId, int type, List<Long> itemIds) {
}
