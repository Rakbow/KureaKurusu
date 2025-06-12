package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/6/12 14:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileCreateDTO extends DTO {

    private int entityType;
    private long entityId;
    private List<Long> fileIds;

}
