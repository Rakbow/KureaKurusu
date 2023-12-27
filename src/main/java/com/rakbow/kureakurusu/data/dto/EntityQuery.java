package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-12-26 16:08
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityQuery extends Query{

    private Integer entityType;
    private Long entityId;

}
