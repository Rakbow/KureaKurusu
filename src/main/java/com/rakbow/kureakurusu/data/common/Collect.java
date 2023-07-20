package com.rakbow.kureakurusu.data.common;

import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-02-22 20:06
 * @Description:
 */
@Data
public class Collect {

    private int entityType;
    private int entityId;
    private long collectCount;

}
