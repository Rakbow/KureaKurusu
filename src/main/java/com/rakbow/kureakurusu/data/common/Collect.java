package com.rakbow.kureakurusu.data.common;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2023-02-22 20:06
 */
@Data
public class Collect {

    private int entityType;
    private int entityId;
    private long collectCount;

}
