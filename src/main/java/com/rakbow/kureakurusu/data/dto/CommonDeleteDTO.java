package com.rakbow.kureakurusu.data.dto;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/6/14 4:51
 */
public record CommonDeleteDTO (int entityType, long entityI, List<Long> ids) {}
