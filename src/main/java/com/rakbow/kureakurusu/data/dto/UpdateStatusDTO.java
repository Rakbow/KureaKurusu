package com.rakbow.kureakurusu.data.dto;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/07 0:03
 */
public record UpdateStatusDTO(int entity, List<Long> ids, boolean status) {
}
