package com.rakbow.kureakurusu.data.result;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/3/7 14:50
 */
@Data
public class EntityStatisticInfo {

    private long count;
    private double ratio;

    public EntityStatisticInfo(long count, long total) {
        this.count = count;
        this.ratio = Math.round(((double) count /total * 100) * 100.0) / 100.0;
    }

}
