package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/5/27 10:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageQueryDTO extends DTO {

    private int entityType;
    private long entityId;
    private int page;
    private int size;

    public ImageQueryDTO() {
        entityType = 0;
        entityId = 0;
        page = 1;
        size = 10;
    }

}
