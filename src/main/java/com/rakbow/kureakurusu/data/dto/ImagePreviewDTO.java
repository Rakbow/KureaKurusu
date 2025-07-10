package com.rakbow.kureakurusu.data.dto;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/7/10 23:55
 */
@Data
public class ImagePreviewDTO {

    private int entityType;
    private long entityId;
    private int count;

}
