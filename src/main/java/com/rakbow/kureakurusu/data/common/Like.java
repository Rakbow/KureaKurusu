package com.rakbow.kureakurusu.data.common;

import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-02-22 20:04
 * @Description:
 */
@Data
public class Like {

    private int entityType;
    private int entityId;
    private long likeCount;

}
