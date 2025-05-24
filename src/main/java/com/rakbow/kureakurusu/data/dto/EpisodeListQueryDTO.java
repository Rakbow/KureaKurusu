package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/5/23 19:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EpisodeListQueryDTO extends ListQueryDTO {

    private String title;
    private Integer episodeType;

    public EpisodeListQueryDTO(ListQuery qry) {
        super(qry);
        title = qry.getVal("title");
        episodeType = qry.getVal("episodeType");
    }

}
