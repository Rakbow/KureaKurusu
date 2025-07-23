package com.rakbow.kureakurusu.data.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/23 16:22
 */
@Data
public class FileRelatedDeleteDTO {

    private int entityType;
    private long entityId;
    private List<Long> ids;

}
