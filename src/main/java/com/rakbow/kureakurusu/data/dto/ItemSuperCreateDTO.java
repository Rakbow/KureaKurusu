package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/1/4 17:00
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class ItemSuperCreateDTO extends DTO {

    private ItemCreateDTO item;
    private List<ImageMiniDTO> images;
    private List<RelatedEntityMiniDTO> relatedEntities;

}
