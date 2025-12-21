package com.rakbow.kureakurusu.data.vo.temp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/12/18 21:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EpisodeSearchVO extends EntitySearchVO {

    private long discId;
    private long discNo;
    private int duration;
    private int serial;

}
