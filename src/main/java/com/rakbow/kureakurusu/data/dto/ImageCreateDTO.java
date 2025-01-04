package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/1/4 17:04
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class ImageCreateDTO extends DTO {

    private int entityType;
    private long entityId;
    private List<ImageMiniDTO> images;

}
