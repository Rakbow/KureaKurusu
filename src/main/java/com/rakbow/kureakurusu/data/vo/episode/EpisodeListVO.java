package com.rakbow.kureakurusu.data.vo.episode;

import com.rakbow.kureakurusu.data.vo.EntityListVO;
import com.rakbow.kureakurusu.data.vo.EntityMiniVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 剧集VO
 *
 * @author Rakbow
 * @since 2024/1/26 14:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EpisodeListVO extends EntityListVO {

    private int discNo;
    private int serial;
    private String premiereDate;
    private String duration;
    private long relatedType;
    private long relatedId;

    private EntityMiniVO parent;

    private int fileCount;
}
