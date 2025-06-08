package com.rakbow.kureakurusu.data.dto;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/6/8 15:16
 */
@Data
public class FileRelatedCreateDTO {

    private int entityType;
    private long entityId;
    private String remark;

}
