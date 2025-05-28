package com.rakbow.kureakurusu.data.vo.episode;

import com.rakbow.kureakurusu.data.vo.EntityMiniVO;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/5/28 14:59
 */
@Data
public class EpisodeRelatedVO {

    private EntityMiniVO parent;
    private List<EpisodeListVO> eps;

}
