package com.rakbow.kureakurusu.data.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/6/8 15:19
 */
@Data
public class FileRelatedBatchCreateDTO {

    private int entityType;
    private long entityId;
    private List<FileRelatedCreateDTO> relations;

}
