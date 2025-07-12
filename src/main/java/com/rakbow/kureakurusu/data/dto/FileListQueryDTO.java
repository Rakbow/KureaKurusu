package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/6/8 7:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileListQueryDTO extends ListQueryDTO {

    private Integer entityType;
    private Integer entityId;

    public void init() {
        super.init();
        entityType = super.getVal("entityType");
        entityId = super.getVal("entityId");
    }

}
