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

    public FileListQueryDTO(ListQuery qry) {
        super(qry);
        entityType = qry.getVal("entityType");
        entityId = qry.getVal("entityId");
    }

}
