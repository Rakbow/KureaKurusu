package com.rakbow.kureakurusu.data.common;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2023-02-22 20:04
 */
@Data
public class Like {

    private int entityType;
    private int entityId;
    private long likeCount;

}
