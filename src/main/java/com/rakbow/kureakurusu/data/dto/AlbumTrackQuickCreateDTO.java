package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/12/23 18:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AlbumTrackQuickCreateDTO extends DTO {

    private long id;
    private AlbumDiscCreateDTO disc;//碟片列表

}
