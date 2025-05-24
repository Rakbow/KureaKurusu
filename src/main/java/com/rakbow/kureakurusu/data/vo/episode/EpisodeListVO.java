package com.rakbow.kureakurusu.data.vo.episode;

import com.rakbow.kureakurusu.data.vo.EntityMiniVO;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 剧集VO
 *
 * @author Rakbow
 * @since 2024/1/26 14:17
 */
@Data
@NoArgsConstructor
public class EpisodeListVO {

    private Long id;
    private int serial;
    private int discNum;
    private String title;
    private String premiereDate;
    private long relatedType;
    private long relatedId;

    private EntityMiniVO parent = new EntityMiniVO();

}
