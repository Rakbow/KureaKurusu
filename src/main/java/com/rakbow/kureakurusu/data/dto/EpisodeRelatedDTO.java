package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/5/28 15:06
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EpisodeRelatedDTO extends DTO {

    private int relatedType;
    private long relatedId;
    private long id;

}
