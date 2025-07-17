package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/17 16:28
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class EntrySuperCreateDTO extends DTO {

    private EntryCreateDTO entry;
    private List<ImageMiniDTO> images;
    private List<RelatedEntityMiniDTO> relatedEntries;

}
